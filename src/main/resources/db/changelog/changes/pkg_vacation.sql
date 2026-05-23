--liquibase formatted sql

--changeset nbp:pkg-vacation-spec dbms:oracle splitStatements:false endDelimiter:/
CREATE OR REPLACE PACKAGE PKG_VACATION AS
    gc_annual_limit CONSTANT NUMBER := 21;

    PROCEDURE submit_vacation_request(
        p_employee_id    IN  NUMBER,
        p_start_date     IN  DATE,
        p_end_date       IN  DATE,
        p_vacation_type  IN  VARCHAR2 DEFAULT NULL,
        p_reason         IN  VARCHAR2 DEFAULT NULL,
        p_vacation_id    OUT NUMBER
    );

    PROCEDURE decide_vacation(
        p_vacation_id  IN NUMBER,
        p_approver_id  IN NUMBER,
        p_approve      IN NUMBER,  -- 1 = approve, 0 = reject (JDBC-friendly)
        p_reason       IN VARCHAR2 DEFAULT NULL
    );

    PROCEDURE calculate_remaining_days(
        p_employee_id    IN  NUMBER,
        p_year           IN  NUMBER DEFAULT NULL,
        p_used_days      OUT NUMBER,
        p_remaining_days OUT NUMBER
    );
END PKG_VACATION;
/

--changeset nbp:pkg-vacation-body dbms:oracle splitStatements:false endDelimiter:/
CREATE OR REPLACE PACKAGE BODY PKG_VACATION AS

    FUNCTION vacation_days(p_start DATE, p_end DATE) RETURN NUMBER IS
    BEGIN
        IF p_start IS NULL OR p_end IS NULL THEN
            RAISE_APPLICATION_ERROR(-20100, 'Start and end date are required.');
        END IF;
        IF p_end < p_start THEN
            RAISE_APPLICATION_ERROR(-20101, 'End date must be on or after start date.');
        END IF;
        RETURN p_end - p_start + 1;
    END vacation_days;

    FUNCTION used_days_for_year(p_employee_id NUMBER, p_year NUMBER) RETURN NUMBER IS
        v_used NUMBER := 0;
    BEGIN
        SELECT NVL(SUM(vacation_days(START_DATE, END_DATE)), 0)
        INTO v_used
        FROM VACATION
        WHERE EMPLOYEE_ID = p_employee_id
          AND EXTRACT(YEAR FROM START_DATE) = p_year
          AND STATUS IN ('PENDING', 'APPROVED');

        RETURN v_used;
    END used_days_for_year;

    PROCEDURE assert_employee_active(p_employee_id NUMBER) IS
        v_status VARCHAR2(100);
    BEGIN
        SELECT STATUS INTO v_status
        FROM EMPLOYMENT
        WHERE EMPLOYEE_ID = p_employee_id;

        IF v_status <> 'ACTIVE' THEN
            RAISE_APPLICATION_ERROR(-20102, 'Employee is not active (status=' || v_status || ').');
        END IF;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20103, 'Employment not found for employee: ' || p_employee_id);
    END assert_employee_active;

    PROCEDURE submit_vacation_request(
        p_employee_id   IN  NUMBER,
        p_start_date    IN  DATE,
        p_end_date      IN  DATE,
        p_vacation_type IN  VARCHAR2,
        p_reason        IN  VARCHAR2,
        p_vacation_id   OUT NUMBER
    ) IS
        v_year          NUMBER;
        v_used          NUMBER;
        v_requested     NUMBER;
        v_overlap       NUMBER;
    BEGIN
        assert_employee_active(p_employee_id);

        v_year := EXTRACT(YEAR FROM p_start_date);
        v_requested := vacation_days(p_start_date, p_end_date);
        v_used := used_days_for_year(p_employee_id, v_year);

        IF v_used + v_requested > gc_annual_limit THEN
            RAISE_APPLICATION_ERROR(-20104,
                'Vacation limit exceeded. Used/requested: ' || (v_used + v_requested) ||
                ', limit: ' || gc_annual_limit);
        END IF;

        SELECT COUNT(*) INTO v_overlap
        FROM VACATION
        WHERE EMPLOYEE_ID = p_employee_id
          AND STATUS IN ('PENDING', 'APPROVED')
          AND p_start_date <= END_DATE
          AND p_end_date >= START_DATE;

        IF v_overlap > 0 THEN
            RAISE_APPLICATION_ERROR(-20105, 'Vacation dates overlap with an existing request.');
        END IF;

        INSERT INTO VACATION (
            EMPLOYEE_ID, START_DATE, END_DATE, VACATION_TYPE, STATUS, REASON
        )
        VALUES (
            p_employee_id, p_start_date, p_end_date, p_vacation_type, 'PENDING', p_reason
        )
        RETURNING ID INTO p_vacation_id;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END submit_vacation_request;

    PROCEDURE decide_vacation(
        p_vacation_id IN NUMBER,
        p_approver_id IN NUMBER,
        p_approve     IN NUMBER,
        p_reason      IN VARCHAR2
    ) IS
        v_status VARCHAR2(255);
        v_emp    NUMBER;
        v_approver_ok NUMBER;
    BEGIN
        SELECT STATUS, EMPLOYEE_ID
        INTO v_status, v_emp
        FROM VACATION
        WHERE ID = p_vacation_id
        FOR UPDATE;

        IF v_status <> 'PENDING' THEN
            RAISE_APPLICATION_ERROR(-20106, 'Only PENDING vacations can be approved or rejected.');
        END IF;

        SELECT COUNT(*) INTO v_approver_ok FROM EMPLOYEE WHERE ID = p_approver_id;
        IF v_approver_ok = 0 THEN
            RAISE_APPLICATION_ERROR(-20107, 'Approver employee does not exist: ' || p_approver_id);
        END IF;

        IF p_approve = 1 THEN
            UPDATE VACATION
            SET STATUS = 'APPROVED',
                APPROVED_BY = p_approver_id,
                REASON = NVL(p_reason, REASON)
            WHERE ID = p_vacation_id;
        ELSE
            IF p_reason IS NULL THEN
                RAISE_APPLICATION_ERROR(-20108, 'Rejection reason is required.');
            END IF;

            UPDATE VACATION
            SET STATUS = 'REJECTED',
                APPROVED_BY = p_approver_id,
                REASON = p_reason
            WHERE ID = p_vacation_id;
        END IF;

        COMMIT;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
            RAISE_APPLICATION_ERROR(-20109, 'Vacation not found: ' || p_vacation_id);
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END decide_vacation;

    PROCEDURE calculate_remaining_days(
        p_employee_id    IN  NUMBER,
        p_year           IN  NUMBER,
        p_used_days      OUT NUMBER,
        p_remaining_days OUT NUMBER
    ) IS
        v_year NUMBER := NVL(p_year, EXTRACT(YEAR FROM SYSDATE));
        v_emp_ok NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_emp_ok FROM EMPLOYEE WHERE ID = p_employee_id;
        IF v_emp_ok = 0 THEN
            RAISE_APPLICATION_ERROR(-20110, 'Employee does not exist: ' || p_employee_id);
        END IF;

        p_used_days := used_days_for_year(p_employee_id, v_year);
        p_remaining_days := gc_annual_limit - p_used_days;

        IF p_remaining_days < 0 THEN
            p_remaining_days := 0;
        END IF;
    END calculate_remaining_days;

END PKG_VACATION;
/

--rollback DROP PACKAGE BODY PKG_VACATION;
--rollback DROP PACKAGE PKG_VACATION;
