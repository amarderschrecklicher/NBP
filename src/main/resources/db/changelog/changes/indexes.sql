--liquibase formatted sql

--changeset ahmed:018
-- Index on vacation(employee_id, start_date) to speed up findByEmployeeIdAndYear queries
CREATE INDEX IDX_VACATION_EMP_START ON VACATION(EMPLOYEE_ID, START_DATE);

--changeset ahmed:019
-- Index on vacation(start_date, end_date) to speed up findByMonthAndYear queries
CREATE INDEX IDX_VACATION_START_END ON VACATION(START_DATE, END_DATE);

