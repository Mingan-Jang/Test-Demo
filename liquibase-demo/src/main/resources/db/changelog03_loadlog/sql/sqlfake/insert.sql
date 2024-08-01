--liquibase formatted sql
--changeset jjk:3_4
insert into create_table_by_another_log (name) values ('meow');
insert into create_table_by_another_log (name) values ('meow2');
insert into create_table_by_another_log (name) values ('meow3');