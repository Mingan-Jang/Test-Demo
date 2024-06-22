-- INDEX 
CREATE INDEX idx_t1 ON jmh_sql_test.sub_index_test  (createtime, gupid, delete_flag);
DROP INDEX idx_t1  ON jmh_sql_test.sub_index_test;



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

-- 對照組一
-- 不使用INDEX，查詢無索引欄位，不符合最左匹配原則
EXPLAIN
SELECT COUNT(*) FROM (
    SELECT creator,status, MAX(status) OVER (PARTITION BY gupid) AS max_status
    FROM jmh_sql_test.sub_index_test sit
    WHERE gupid = 'group5' 
    AND createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00'
    AND delete_flag = false
) AS subquery
WHERE status = max_status;


-- 強制使用INDEX，查詢有索引欄位，WHERE條件符合最左匹配原則，因為覆蓋到全部索引
EXPLAIN
SELECT COUNT(*) FROM (
    SELECT gupid, status ,MAX(status) OVER (PARTITION BY gupid) AS max_status
    FROM jmh_sql_test.sub_index_test sit FORCE INDEX (idx_t1)
    WHERE gupid = 'group5'
    AND createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00'
    AND delete_flag = false
) AS subquery
WHERE status = max_status;

-- 對照組二
-- 強制使用INDEX，查詢有索引欄位，WHERE條件符合最左匹配原則，因為覆蓋到第一個索引
EXPLAIN
SELECT COUNT(*) FROM (
    SELECT gupid, status ,MAX(status) OVER (PARTITION BY gupid) AS max_status
    FROM jmh_sql_test.sub_index_test sit FORCE INDEX (idx_t1)
    WHERE  createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00'
) AS subquery
WHERE status = max_status;


-- 不使用INDEX，查詢有索引欄位
EXPLAIN
SELECT COUNT(*) FROM (
    SELECT gupid, status ,MAX(status) OVER (PARTITION BY gupid) AS max_status
    FROM jmh_sql_test.sub_index_test sit 
    WHERE  createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00'
) AS subquery
WHERE status = max_status;



-- 對照組三
-- 不使用INDEX，查詢所有欄位，WHERE條件符合最左匹配原則
EXPLAIN
SELECT COUNT(*) FROM (
    SELECT * ,MAX(status) OVER (PARTITION BY gupid) AS max_status
    FROM jmh_sql_test.sub_index_test sit 
    WHERE  createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00'
) AS subquery
WHERE status = max_status;

-- 強制使用INDEX，查詢所有欄位，WHERE條件符合最左匹配原則
EXPLAIN
SELECT COUNT(*) FROM (
    SELECT * ,MAX(status) OVER (PARTITION BY gupid) AS max_status
    FROM jmh_sql_test.sub_index_test sit FORCE INDEX (idx_t1)
    WHERE  createtime >= '2022-06-17 01:00:00' AND createtime <= '2023-12-17 01:00:00'
) AS subquery
WHERE status = max_status;