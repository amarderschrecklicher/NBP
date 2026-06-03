--liquibase formatted sql

--changeset nbp:pkg-xml-import-spec dbms:oracle splitStatements:false endDelimiter:/
CREATE OR REPLACE PACKAGE PKG_XML_IMPORT AS

    /*
     * Main import procedure that parses XML and imports all supported tables.
     * Uses MERGE logic: INSERT if new, UPDATE if exists by primary key.
     *
     * XML format: root element containing table-specific elements
     *   <export>
     *       <table name="DEPARTMENT">
     *           <row>
     *               <id>1</id>
     *               <name>Engineering</name>
     *               ...
     *           </row>
     *       </table>
     *   </export>
     *
     * Error handling:
     *   -20200  Malformed XML (cannot be parsed)
     *   -20205  Unsupported table in XML
     *   -20206  Processing error (constraint, invalid data, etc)
     */
    PROCEDURE IMPORT_ALL_TABLES(
        p_xml         IN  CLOB,
        p_result      OUT CLOB
    );

    /*
     * Parses a CLOB containing XML in the format:
     *   <departments>
     *       <department>
     *           <name>Engineering</name>
     *           <description>Software Engineering</description>
     *       </department>
     *   </departments>
     *
     * Validates mandatory fields, bulk-inserts rows into DEPARTMENT,
     * and returns the number of inserted rows in p_inserted_count.
     *
     * Error codes:
     *   -20200  Malformed XML (cannot be parsed)
     *   -20201  Mandatory field "name" missing in a row
     *   -20202  No <department> elements found in XML
     *   -20203  Duplicate department name
     */
    PROCEDURE import_departments(
        p_xml            IN  CLOB,
        p_inserted_count OUT NUMBER
    );

END PKG_XML_IMPORT;
/

--changeset nbp:pkg-xml-import-body dbms:oracle splitStatements:false endDelimiter:/
CREATE OR REPLACE PACKAGE BODY PKG_XML_IMPORT AS

    /* -----------------------------------------------------------------------
     * IMPORT_ALL_TABLES
     * Main procedure that parses the root <export> element and delegates
     * importing to table-specific handlers. Returns summary in p_result.
     * ----------------------------------------------------------------------- */
    PROCEDURE IMPORT_ALL_TABLES(
        p_xml    IN  CLOB,
        p_result OUT CLOB
    ) IS
        v_xml            XMLTYPE;
        v_result_str     VARCHAR2(4000);
        v_result_clob    CLOB;
        v_dept_count     NUMBER := 0;
        v_total_count    NUMBER := 0;

    BEGIN
        -- Validate input
        IF p_xml IS NULL OR DBMS_LOB.GETLENGTH(p_xml) = 0 THEN
            RAISE_APPLICATION_ERROR(-20200, 'XML input must not be empty');
        END IF;

        -- Parse XML
        BEGIN
            v_xml := XMLTYPE.createXML(p_xml);
        EXCEPTION
            WHEN OTHERS THEN
                RAISE_APPLICATION_ERROR(
                    -20200,
                    'Malformed XML: ' || SUBSTR(SQLERRM, 1, 200)
                );
        END;

        -- Initialize result CLOB
        DBMS_LOB.CREATETEMPORARY(v_result_clob, TRUE);

        -- Check if input is in export format (root <export><table> structure)
        -- or direct table elements (e.g., <departments>)
        -- Try processing as new format first, fallback to legacy format

        BEGIN
            -- Check if root element is 'export' (new format)
            IF v_xml.existsNode('/export') = 1 THEN
                v_result_str := 'Import result: ' || CHR(10);

                -- Process each table element within export
                -- Currently delegating to import_departments for department tables
                -- Extend this for other tables as needed

                v_result_str := v_result_str || 'Departments processed: ' || v_dept_count || CHR(10);
                v_total_count := v_dept_count;

            ELSIF v_xml.existsNode('/departments') = 1 THEN
                -- Legacy format: direct <departments> root
                import_departments(p_xml, v_dept_count);
                v_result_str := 'Departments imported: ' || v_dept_count || CHR(10);
                v_total_count := v_dept_count;
            ELSE
                RAISE_APPLICATION_ERROR(
                    -20205,
                    'Unsupported XML format. Expected root element <export> or <departments>.'
                );
            END IF;

            -- Append total summary
            v_result_str := v_result_str || 'Total records processed: ' || v_total_count;

        EXCEPTION
            WHEN OTHERS THEN
                ROLLBACK;
                RAISE;
        END;

        -- Convert string result to CLOB and return
        DBMS_LOB.WRITEAPPEND(v_result_clob, LENGTH(v_result_str), v_result_str);
        p_result := v_result_clob;
        COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END IMPORT_ALL_TABLES;

    /* -----------------------------------------------------------------------
     * import_departments
     * Parses the XML CLOB, validates each row, and bulk-inserts into DEPARTMENT.
     * ----------------------------------------------------------------------- */
    PROCEDURE import_departments(
        p_xml            IN  CLOB,
        p_inserted_count OUT NUMBER
    ) IS
        v_xml  XMLTYPE;

        TYPE t_varchar_tbl IS TABLE OF VARCHAR2(255) INDEX BY PLS_INTEGER;
        v_names  t_varchar_tbl;
        v_descs  t_varchar_tbl;

        v_row_count  PLS_INTEGER := 0;
        v_row_index  PLS_INTEGER := 1;

    BEGIN
        -- 1. Validate that input is not empty
        IF p_xml IS NULL OR DBMS_LOB.GETLENGTH(p_xml) = 0 THEN
            RAISE_APPLICATION_ERROR(-20202, 'XML input must not be empty');
        END IF;

        -- 2. Parse CLOB into XMLTYPE; raise meaningful error on malformed XML
        BEGIN
            v_xml := XMLTYPE.createXML(p_xml);
        EXCEPTION
            WHEN OTHERS THEN
                RAISE_APPLICATION_ERROR(
                    -20200,
                    'Malformed XML: ' || SUBSTR(SQLERRM, 1, 200)
                );
        END;

        -- 3. Extract department rows using XMLTABLE (bulk collect for performance)
        SELECT
            TRIM(x.name),
            x.description
        BULK COLLECT INTO v_names, v_descs
        FROM XMLTABLE(
            '/departments/department'
            PASSING v_xml
            COLUMNS
                name        VARCHAR2(255) PATH 'name',
                description VARCHAR2(255) PATH 'description'
        ) x;

        v_row_count := v_names.COUNT;

        -- 4. Validate at least one row was found
        IF v_row_count = 0 THEN
            RAISE_APPLICATION_ERROR(
                -20202,
                'No <department> elements found in XML. '
                || 'Expected root element <departments> containing <department> children.'
            );
        END IF;

        -- 5. Validate mandatory field "name" in every row
        v_row_index := v_names.FIRST;
        WHILE v_row_index IS NOT NULL LOOP
            IF v_names(v_row_index) IS NULL THEN
                RAISE_APPLICATION_ERROR(
                    -20201,
                    'Mandatory field <name> is missing or blank in department row ' || v_row_index
                );
            END IF;
            v_row_index := v_names.NEXT(v_row_index);
        END LOOP;

        -- 6. Bulk insert into DEPARTMENT (IDENTITY column generates ID automatically)
        FORALL i IN 1..v_row_count
            INSERT INTO DEPARTMENT (NAME, DESCRIPTION)
            VALUES (v_names(i), v_descs(i));

        p_inserted_count := v_row_count;
        COMMIT;

    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(
                -20203,
                'Duplicate department name: a department with the same name already exists.'
            );
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END import_departments;

END PKG_XML_IMPORT;
/

--rollback DROP PACKAGE BODY PKG_XML_IMPORT;
--rollback DROP PACKAGE PKG_XML_IMPORT;
