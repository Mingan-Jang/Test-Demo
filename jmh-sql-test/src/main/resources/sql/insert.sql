USE jmh_sql_test;

CALL increase_12000();

SELECT COUNT(*) FROM jmh_sql_test.sub_index_test ;

-- 創建沒有index的表
CREATE TABLE jmh_sql_test.no_index_test AS SELECT * FROM sub_index_test;
