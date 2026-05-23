--liquibase formatted sql

--changeset nbp:pkg-employee-spec dbms:oracle splitStatements:false endDelimiter:/
CREATE OR REPLACE PACKAGE PKG_EMPLOYEE AS
    gc_active_status CONSTANT VARCHAR2(20) := 'ACTIVE';

    PROCEDURE add_employee(
        p_user_id           IN  NUMBER,
        p_gender            IN  VARCHAR2 DEFAULT NULL,
        p_nationality       IN  VARCHAR2 DEFAULT NULL,
        p_marital_status    IN  VARCHAR2 DEFAULT NULL,
        p_manager_id        IN  NUMBER   DEFAULT NULL,
        p_employment_number IN  VARCHAR2,
        p_hire_date         IN  DATE     DEFAULT TRUNC(SYSDATE),
        p_job_title         IN  VARCHAR2 DEFAULT NULL,
        p_employment_type   IN  VARCHAR2 DEFAULT NULL,
        p_department_id     IN  NUMBER   DEFAULT NULL,
        p_employee_id       OUT NUMBER,
        p_employment_id     OUT NUMBER
    );

    PROCEDURE update_employment(
        p_employee_id     IN NUMBER,
        p_status          IN VARCHAR2 DEFAULT NULL,
        p_department_id   IN NUMBER   DEFAULT NULL
    );

    PROCEDURE archive_employee(
        p_employee_id   IN NUMBER,
        p_hard_delete   IN NUMBER DEFAULT 0  -- 0 = terminate, 1 = hard delete (JDBC-friendly)
    );
END PKG_EMPLOYEE;
/

--changeset nbp:pkg-employee-body dbms:oracle splitStatements:false endDelimiter:/
CREATE OR REPLACE PACKAGE BODY PKG_EMPLOYEE AS

    PROCEDURE assert_user_exists(p_user_id NUMBER) IS
        v_cnt NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_cnt FROM NBP.NBP_USER WHERE ID = p_user_id;
        IF v_cnt = 0 THEN
            RAISE_APPLICATION_ERROR(-20010, 'NBP_USER does not exist: ' || p_user_id);
        END IF;
    END;

    PROCEDURE assert_department_exists(p_department_id NUMBER) IS
        v_cnt NUMBER;
    BEGIN
        IF p_department_id IS NULL THEN
            RETURN;
        END IF;
        SELECT COUNT(*) INTO v_cnt FROM DEPARTMENT WHERE ID = p_department_id;
        IF v_cnt = 0 THEN
            RAISE_APPLICATION_ERROR(-20011, 'Department does not exist: ' || p_department_id);
        END IF;
    END;

    PROCEDURE assert_valid_status(p_status VARCHAR2) IS
    BEGIN
        IF p_status IS NOT NULL
           AND p_status NOT IN ('ACTIVE', 'INACTIVE', 'ON_LEAVE', 'TERMINATED', 'SUSPENDED', 'PROBATION') THEN
            RAISE_APPLICATION_ERROR(-20012, 'Invalid employment status: ' || p_status);
        END IF;
    END;

    PROCEDURE add_employee(
        p_user_id           IN  NUMBER,
        p_gender            IN  VARCHAR2,
        p_nationality       IN  VARCHAR2,
        p_marital_status    IN  VARCHAR2,
        p_manager_id        IN  NUMBER,
        p_employment_number IN  VARCHAR2,
        p_hire_date         IN  DATE,
        p_job_title         IN  VARCHAR2,
        p_employment_type   IN  VARCHAR2,
        p_department_id     IN  NUMBER,
        p_employee_id       OUT NUMBER,
        p_employment_id     OUT NUMBER
    ) IS
        v_user_taken NUMBER;
        v_manager_ok NUMBER;
    BEGIN
        assert_user_exists(p_user_id);
        assert_department_exists(p_department_id);

        SELECT COUNT(*) INTO v_user_taken
        FROM EMPLOYEE
        WHERE USER_ID = p_user_id;

        IF v_user_taken > 0 THEN
            RAISE_APPLICATION_ERROR(-20013, 'User is already linked to an employee.');
        END IF;

        IF p_manager_id IS NOT NULL THEN
            SELECT COUNT(*) INTO v_manager_ok FROM EMPLOYEE WHERE ID = p_manager_id;
            IF v_manager_ok = 0 THEN
                RAISE_APPLICATION_ERROR(-20014, 'Manager does not exist: ' || p_manager_id);
            END IF;
        END IF;

        IF p_employment_number IS NULL THEN
            RAISE_APPLICATION_ERROR(-20015, 'Employment number is required.');
        END IF;

        INSERT INTO EMPLOYEE (ID, USER_ID, GENDER, NATIONALITY, MARITAL_STATUS, MANAGER_ID)
        VALUES (EMPLOYEE_SEQ.NEXTVAL, p_user_id, p_gender, p_nationality, p_marital_status, p_manager_id)
        RETURNING ID INTO p_employee_id;

        INSERT INTO EMPLOYMENT (
            ID, EMPLOYEE_ID, EMPLOYMENT_NUMBER, HIRE_DATE,
            JOB_TITLE, EMPLOYMENT_TYPE, STATUS, DEPARTMENT_ID
        )
        VALUES (
            EMPLOYMENT_SEQ.NEXTVAL, p_employee_id, p_employment_number, NVL(p_hire_date, TRUNC(SYSDATE)),
            p_job_title, p_employment_type, gc_active_status, p_department_id
        )
        RETURNING ID INTO p_employment_id;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END add_employee;

    PROCEDURE update_employment(
        p_employee_id   IN NUMBER,
        p_status        IN VARCHAR2,
        p_department_id IN NUMBER
    ) IS
        v_cnt NUMBER;
    BEGIN
        IF p_status IS NULL AND p_department_id IS NULL THEN
            RAISE_APPLICATION_ERROR(-20016, 'Provide at least status or department_id to update.');
        END IF;

        assert_valid_status(p_status);
        assert_department_exists(p_department_id);

        SELECT COUNT(*) INTO v_cnt FROM EMPLOYEE WHERE ID = p_employee_id;
        IF v_cnt = 0 THEN
            RAISE_APPLICATION_ERROR(-20017, 'Employee does not exist: ' || p_employee_id);
        END IF;

        UPDATE EMPLOYMENT
        SET STATUS = NVL(p_status, STATUS),
            DEPARTMENT_ID = NVL(p_department_id, DEPARTMENT_ID)
        WHERE EMPLOYEE_ID = p_employee_id;

        IF SQL%ROWCOUNT = 0 THEN
            RAISE_APPLICATION_ERROR(-20018, 'Employment record not found for employee: ' || p_employee_id);
        END IF;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END update_employment;

    PROCEDURE archive_employee(
        p_employee_id IN NUMBER,
        p_hard_delete IN NUMBER
    ) IS
        v_pending_vacations NUMBER;
        v_subordinates      NUMBER;
        v_status            VARCHAR2(100);
    BEGIN
        SELECT e.STATUS INTO v_status
        FROM EMPLOYMENT e
        WHERE e.EMPLOYEE_ID = p_employee_id;

        IF v_status = 'TERMINATED' THEN
            RAISE_APPLICATION_ERROR(-20019, 'Employee is already terminated.');
        END IF;

        SELECT COUNT(*) INTO v_pending_vacations
        FROM VACATION
        WHERE EMPLOYEE_ID = p_employee_id
          AND STATUS = 'PENDING';

        IF v_pending_vacations > 0 THEN
            RAISE_APPLICATION_ERROR(-20020, 'Cannot archive employee with pending vacation requests.');
        END IF;

        SELECT COUNT(*) INTO v_subordinates
        FROM EMPLOYEE
        WHERE MANAGER_ID = p_employee_id;

        IF v_subordinates > 0 THEN
            RAISE_APPLICATION_ERROR(-20021, 'Cannot archive employee who is manager of other employees.');
        END IF;

        IF p_hard_delete = 1 THEN
            DELETE FROM EMPLOYMENT WHERE EMPLOYEE_ID = p_employee_id;
            DELETE FROM EMPLOYEE WHERE ID = p_employee_id;
        ELSE
            UPDATE EMPLOYMENT
            SET STATUS = 'TERMINATED',
                TERMINATION_DATE = TRUNC(SYSDATE)
            WHERE EMPLOYEE_ID = p_employee_id;
        END IF;

        COMMIT;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20022, 'Employee or employment not found: ' || p_employee_id);
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END archive_employee;

END PKG_EMPLOYEE;
/

--rollback DROP PACKAGE BODY PKG_EMPLOYEE;
--rollback DROP PACKAGE PKG_EMPLOYEE;
