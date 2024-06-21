USE jmh_sql_test;


CALL increase_12000();


SELECT COUNT(*) FROM jmh_sql_test.sub_index_test sit ;
