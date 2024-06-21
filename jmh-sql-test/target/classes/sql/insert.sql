USE jmh_sql_test;


-- 先CALL 3000筆測試
CALL increase_1000();


SELECT COUNT(*) FROM jmh_sql_test.sub_index_test sit ;

-- CALL 100000筆測試
CALL increase_100000();

SELECT COUNT(*) FROM jmh_sql_test.sub_index_test sit ;
