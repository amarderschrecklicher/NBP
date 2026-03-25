--liquibase formatted sql

--changeset nejra:002
CREATE TABLE NBPT9.ADDRESS (
                               ID NUMBER PRIMARY KEY,
                               STREET VARCHAR2(255),
                               CITY VARCHAR2(255),
                               POSTAL_CODE VARCHAR2(50),
                               COUNTRY VARCHAR2(100)
);

CREATE SEQUENCE NBPT9.ADDRESS_SEQ START WITH 1 INCREMENT BY 1;

--rollback DROP SEQUENCE NBPT9.ADDRESS_SEQ;
--rollback DROP TABLE NBPT9.ADDRESS;
