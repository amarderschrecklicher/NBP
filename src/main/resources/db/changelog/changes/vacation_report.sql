--liquibase formatted sql

--changeset nejra:019
CREATE TABLE VACATION_REPORT (
                                 ID            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 REPORT_MONTH  NUMBER(2) NOT NULL,
                                 REPORT_YEAR   NUMBER(4) NOT NULL,
                                 PDF_CONTENT   BLOB NOT NULL,
                                 GENERATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 GENERATED_BY  NUMBER,
                                 CONSTRAINT UQ_REPORT_MONTH_YEAR UNIQUE (REPORT_MONTH, REPORT_YEAR)
);
