-- INDEX 
DROP INDEX IF EXISTS idx_t1  ON jmh_sql_test.sub_index_test;
CREATE INDEX idx_t1 ON jmh_sql_test.sub_index_test  (createtime, gupid, delete_flag);
SHOW INDEX FROM jmh_sql_test.sub_index_test;

-- -- 測試INDEX
EXPLAIN
SELECT gupid
    FROM jmh_sql_test.sub_index_test sit
    WHERE createtime >= '2023-06-17 01:00:00'  
    AND gupid = 'group2' 
    AND delete_flag = false;

-- -- 測試INDEX(因*導致索引失效, 也可是因為選到沒有的非複合索引的欄位)
EXPLAIN
SELECT *
    FROM jmh_sql_test.sub_index_test sit
    WHERE createtime >= '2023-06-17 01:00:00'  
    AND gupid = 'group2' 
    AND delete_flag = false;
    
    

-- SEARCH
-- 沒有使用INDEX
EXPLAIN
SELECT COUNT(*) FROM (
    SELECT creator,status, MAX(status) OVER (PARTITION BY gupid) AS max_status
    FROM jmh_sql_test.sub_index_test sit
    WHERE gupid = 'group2' 
    AND createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00'
    AND delete_flag = false
) AS subquery
WHERE status = max_status;


-- 有使用INDEX
EXPLAIN
SELECT COUNT(*) FROM (
    SELECT gupid, status ,MAX(status) OVER (PARTITION BY gupid) AS max_status
    FROM jmh_sql_test.sub_index_test sit FORCE INDEX (idx_t1)
    WHERE gupid = 'group2' 
    AND createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00'
    AND delete_flag = false
) AS subquery
WHERE status = max_status;


