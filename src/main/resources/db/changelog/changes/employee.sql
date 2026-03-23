--liquibase formatted sql

--changeset amar:001
CREATE TABLE EMPLOYEE (
                          ID NUMBER PRIMARY KEY,
                          USER_ID NUMBER NOT NULL UNIQUE,
                          FIRST_NAME VARCHAR2(255) NOT NULL,
                          LAST_NAME VARCHAR2(255) NOT NULL,
                          DATE_OF_BIRTH DATE,
                          GENDER VARCHAR2(50),
                          NATIONALITY VARCHAR2(100),
                          MARITAL_STATUS VARCHAR2(50)
);

ALTER TABLE EMPLOYEE
    ADD CONSTRAINT FK_EMPLOYEE_USER
        FOREIGN KEY (USER_ID)
            REFERENCES NBP.NBP_USER(ID);

CREATE SEQUENCE EMPLOYEE_SEQ START WITH 1 INCREMENT BY 1;