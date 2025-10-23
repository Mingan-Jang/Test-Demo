--liquibase formatted sql
--changeset techgeeknext:create-tables

INSERT INTO liquiTable (id, leaf, parent) VALUES ('1', 0, NULL);
INSERT INTO liquiTable (id, leaf, parent) VALUES ('2', 1, '1');
INSERT INTO liquiTable (id, leaf, parent) VALUES ('3', 2, '2');
INSERT INTO liquiTable (id, leaf, parent) VALUES ('4', 2, '2');
INSERT INTO liquiTable (id, leaf, parent) VALUES ('5', 3, '3');
INSERT INTO liquiTable (id, leaf, parent) VALUES ('6', 1, '1');
INSERT INTO liquiTable (id, leaf, parent) VALUES ('7', 2, '2');
INSERT INTO liquiTable (id, leaf, parent) VALUES ('8', 2, '4');