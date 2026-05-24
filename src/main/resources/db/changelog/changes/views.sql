--liquibase formatted sql

--changeset copilot:views-001
-- ============================================================================
-- VIEW: VW_EMPLOYEE_WORK_PROFILE
-- ============================================================================
-- Purpose:
--   Provides a comprehensive HR profile for each employee combining identity,
--   employment, organizational hierarchy, and department information.
--
-- Use Cases:
--   - HR dashboards showing employee details with manager and department
--   - Employee directory listings
--   - Organizational structure views
--   - Employment status tracking (days since hire, active/inactive)
--
-- Key Columns:
--   - employee_id, user_id: Employee and user identifiers
--   - employee_full_name, manager_full_name: Concatenated names from NBP_USER
--   - gender, nationality, marital_status: Personal demographic data
--   - employment_id, job_title, employment_type: Employment details
--   - hire_date, termination_date, days_since_hire: Service tracking
--   - department_name: Organizational unit
--   - lifecycle_state: Computed field (ACTIVE/INACTIVE) based on termination date
--
-- Data Sources:
--   - EMPLOYEE (core employee record)
--   - NBP.NBP_USER (user profile with names and contact info)
--   - EMPLOYMENT (job and department assignment)
--   - DEPARTMENT (organizational structure)
-- ============================================================================
CREATE OR REPLACE VIEW VW_EMPLOYEE_WORK_PROFILE AS
SELECT
    e.id AS employee_id,
    e.user_id,
    u.username,
    u.first_name || ' ' || u.last_name AS employee_full_name,
    u.email,
    u.phone_number,
    u.birth_date,
    e.gender,
    e.nationality,
    e.marital_status,
    e.manager_id,
    mgr_u.first_name || ' ' || mgr_u.last_name AS manager_full_name,
    emp.id AS employment_id,
    emp.employment_number,
    emp.hire_date,
    emp.termination_date,
    CASE
        WHEN emp.hire_date IS NOT NULL THEN TRUNC(SYSDATE) - TRUNC(emp.hire_date)
    END AS days_since_hire,
    emp.job_title,
    emp.employment_type,
    emp.status AS employment_status,
    dept.id AS department_id,
    dept.name AS department_name,
    CASE
        WHEN emp.termination_date IS NULL THEN 'ACTIVE'
        ELSE 'INACTIVE'
    END AS lifecycle_state
FROM employee e
JOIN nbp.nbp_user u ON u.id = e.user_id
LEFT JOIN employee mgr ON mgr.id = e.manager_id
LEFT JOIN nbp.nbp_user mgr_u ON mgr_u.id = mgr.user_id
LEFT JOIN employment emp ON emp.employee_id = e.id
LEFT JOIN department dept ON dept.id = emp.department_id;

--rollback DROP VIEW VW_EMPLOYEE_WORK_PROFILE;

--changeset copilot:views-002
-- ============================================================================
-- VIEW: VW_PAYROLL_COST_OVERVIEW
-- ============================================================================
-- Purpose:
--   Aggregates payroll, compensation, and cost-center information for
--   financial planning, budget forecasting, and compensation analysis.
--
-- Use Cases:
--   - Payroll cost reporting and budget projections
--   - Compensation band analysis and equity audits
--   - Department-level cost center summaries
--   - Bonus eligibility and payment frequency tracking
--   - Annual salary equivalent calculations
--
-- Key Columns:
--   - finance_id, employee_id: Finance record and employee identifiers
--   - employee_full_name, job_title, department_name: Context fields
--   - salary, currency, payment_frequency: Compensation details
--   - annual_salary_equivalent: Derived field normalizing all frequencies to annual
--   - compensation_band: Categorical salary level (ENTRY_LEVEL/MID_MARKET/SENIOR_COST)
--   - bonus_eligible_flag: YES/NO based on bonus_eligible flag
--   - employment_status: Current employment state
--   - bank_name, iban, tax_number: Financial institution details
--
-- Data Sources:
--   - FINANCE (compensation and payment info)
--   - EMPLOYEE + NBP.NBP_USER (employee identity)
--   - EMPLOYMENT + DEPARTMENT (organizational context)
--
-- Notes:
--   - Salary is normalized from various payment frequencies (WEEKLY, BIWEEKLY, MONTHLY, DAILY)
--   - Uses 260 working days/year for daily rates, 52 weeks for weekly, etc.
-- ============================================================================
CREATE OR REPLACE VIEW VW_PAYROLL_COST_OVERVIEW AS
SELECT
    f.id AS finance_id,
    e.id AS employee_id,
    u.username,
    u.first_name || ' ' || u.last_name AS employee_full_name,
    emp.job_title,
    dept.name AS department_name,
    f.bank_name,
    f.bank_account_number,
    f.iban,
    f.tax_number,
    f.salary,
    f.currency,
    f.payment_frequency,
    CASE
        WHEN f.salary IS NULL THEN NULL
        WHEN UPPER(NVL(f.payment_frequency, 'MONTHLY')) = 'MONTHLY' THEN f.salary * 12
        WHEN UPPER(f.payment_frequency) = 'WEEKLY' THEN f.salary * 52
        WHEN UPPER(f.payment_frequency) = 'BIWEEKLY' THEN f.salary * 26
        WHEN UPPER(f.payment_frequency) = 'DAILY' THEN f.salary * 260
        ELSE f.salary
    END AS annual_salary_equivalent,
    CASE
        WHEN NVL(f.bonus_eligible, 0) = 1 THEN 'YES'
        ELSE 'NO'
    END AS bonus_eligible_flag,
    CASE
        WHEN f.salary IS NULL THEN 'UNSPECIFIED'
        WHEN f.salary >= 5000 THEN 'SENIOR_COST'
        WHEN f.salary >= 2500 THEN 'MID_MARKET'
        ELSE 'ENTRY_LEVEL'
    END AS compensation_band,
    CASE
        WHEN emp.status IS NULL THEN 'NO_EMPLOYMENT_RECORD'
        ELSE UPPER(emp.status)
    END AS employment_status
FROM finance f
JOIN employee e ON e.id = f.employee_id
JOIN nbp.nbp_user u ON u.id = e.user_id
LEFT JOIN employment emp ON emp.employee_id = e.id
LEFT JOIN department dept ON dept.id = emp.department_id;

--rollback DROP VIEW VW_PAYROLL_COST_OVERVIEW;

--changeset copilot:views-003
-- ============================================================================
-- VIEW: VW_VACATION_REQUEST_OVERVIEW
-- ============================================================================
-- Purpose:
--   Consolidates vacation request data with approver information, request
--   durations, and window-function aggregates for compliance and planning.
--
-- Use Cases:
--   - Vacation request approval workflows and status dashboards
--   - Year-to-date vacation day accounting (per employee, per year)
--   - Department vacation coverage analysis
--   - Vacation policy enforcement (tracking requests_this_year, requested_days_this_year)
--   - Open vs. closed request counts
--
-- Key Columns:
--   - vacation_id, employee_id: Vacation request identifier
--   - employee_full_name, department_name, job_title: Employee context
--   - start_date, end_date: Request date range
--   - total_days: Derived field (end_date - start_date + 1)
--   - vacation_year, vacation_month: Extracted from start_date for grouping
--   - vacation_type, status: Request classification and state
--   - approved_by_full_name: Name of approver (manager/HR)
--   - workflow_bucket: Computed as OPEN (pending) or CLOSED (approved/rejected)
--   - requests_this_year: Window count of all requests by same employee, same year
--   - requested_days_this_year: Window sum of all requested days by employee, year
--
-- Data Sources:
--   - VACATION (request records)
--   - EMPLOYEE + NBP.NBP_USER (employee identity)
--   - EMPLOYMENT + DEPARTMENT (organizational context)
--   - Approver lookups via EMPLOYEE + NBP.NBP_USER (manager chain)
--
-- Notes:
--   - Uses PARTITION BY and SUM/COUNT window functions for aggregates
--   - Supports compliance checks (max requests/year, max days/year policies)
-- ============================================================================
CREATE OR REPLACE VIEW VW_VACATION_REQUEST_OVERVIEW AS
SELECT
    v.id AS vacation_id,
    v.employee_id,
    u.first_name || ' ' || u.last_name AS employee_full_name,
    u.email AS employee_email,
    dept.name AS department_name,
    emp.job_title,
    v.start_date,
    v.end_date,
    CASE
        WHEN v.start_date IS NOT NULL AND v.end_date IS NOT NULL
            THEN TRUNC(v.end_date) - TRUNC(v.start_date) + 1
    END AS total_days,
    EXTRACT(YEAR FROM v.start_date) AS vacation_year,
    EXTRACT(MONTH FROM v.start_date) AS vacation_month,
    v.vacation_type,
    v.status,
    v.approved_by,
    appr_u.first_name || ' ' || appr_u.last_name AS approved_by_full_name,
    CASE
        WHEN UPPER(v.status) IN ('APPROVED', 'REJECTED') THEN 'CLOSED'
        ELSE 'OPEN'
    END AS workflow_bucket,
    COUNT(*) OVER (
        PARTITION BY v.employee_id, EXTRACT(YEAR FROM v.start_date)
    ) AS requests_this_year,
    SUM(
        CASE
            WHEN v.start_date IS NOT NULL AND v.end_date IS NOT NULL
                THEN TRUNC(v.end_date) - TRUNC(v.start_date) + 1
        END
    ) OVER (
        PARTITION BY v.employee_id, EXTRACT(YEAR FROM v.start_date)
    ) AS requested_days_this_year
FROM vacation v
JOIN employee e ON e.id = v.employee_id
JOIN nbp.nbp_user u ON u.id = e.user_id
LEFT JOIN employment emp ON emp.employee_id = e.id
LEFT JOIN department dept ON dept.id = emp.department_id
LEFT JOIN employee appr ON appr.id = v.approved_by
LEFT JOIN nbp.nbp_user appr_u ON appr_u.id = appr.user_id;

--rollback DROP VIEW VW_VACATION_REQUEST_OVERVIEW;



