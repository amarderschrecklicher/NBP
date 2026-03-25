--liquibase formatted sql

--changeset amar:002
CREATE TABLE EMPLOYMENT (
                            ID NUMBER PRIMARY KEY,
                            EMPLOYEE_ID NUMBER NOT NULL UNIQUE,
                            EMPLOYMENT_NUMBER VARCHAR2(100),
                            HIRE_DATE DATE,
                            TERMINATION_DATE DATE,
                            JOB_TITLE VARCHAR2(255),
                            EMPLOYMENT_TYPE VARCHAR2(100),
                            STATUS VARCHAR2(100),
                            DEPARTMENT_ID NUMBER
);

ALTER TABLE EMPLOYMENT
    ADD CONSTRAINT FK_EMPLOYMENT_EMPLOYEE
        FOREIGN KEY (EMPLOYEE_ID)
            REFERENCES EMPLOYEE(ID);

CREATE SEQUENCE EMPLOYMENT_SEQ START WITH 1 INCREMENT BY 1;

--changeset ahmed:004
ALTER TABLE EMPLOYMENT
    ADD CONSTRAINT FK_EMPLOYMENT_DEPARTMENT
        FOREIGN KEY (DEPARTMENT_ID)
            REFERENCES department(id);
