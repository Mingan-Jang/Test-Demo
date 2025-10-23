--liquibase formatted sql
--changeset techgeeknext:create-tables

CREATE TABLE IF NOT EXISTS liquiTable (
    id VARCHAR(40) PRIMARY KEY,
    leaf INT NOT NULL DEFAULT 1,
    parent VARCHAR(40)
);
