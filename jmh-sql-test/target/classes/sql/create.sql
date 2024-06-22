CREATE DATABASE IF NOT EXISTS jmh_sql_test;


CREATE TABLE IF NOT EXISTS jmh_sql_test.sub_index_test (
  `oid` CHAR(36) NOT NULL COMMENT 'UUID',
  `status` INT(11) DEFAULT NULL COMMENT '每個群組ID都有不同狀態',
  `gupid` VARCHAR(20) DEFAULT NULL COMMENT '群組ID',
  `delete_flag` BOOLEAN COMMENT '是否刪除',
  `creator` VARCHAR(255) DEFAULT NULL COMMENT '創建者',
  `createtime` TIMESTAMP DEFAULT NULL,
  `comment` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


USE jmh_sql_test;

DELIMITER //

CREATE PROCEDURE IF NOT EXISTS increase_12000()
BEGIN
    DECLARE i INT DEFAULT 0;
    WHILE i < 12000 DO
        INSERT INTO sub_index_test (`oid`, `status`, `gupid`, `delete_flag`, `creator`, `createtime`, `comment`)
        VALUES
        (UUID(),
            FLOOR(RAND() * 8),
            CONCAT('group', FLOOR(RAND() * 21)),
            IF(RAND() < 0.5, 0, 1),
            CONCAT('creator', FLOOR(RAND() * 8) + 1),
            FROM_UNIXTIME(UNIX_TIMESTAMP('2022-01-01 00:00:00') + FLOOR(RAND() * (UNIX_TIMESTAMP('2024-12-31 23:59:59') - UNIX_TIMESTAMP('2022-01-01 00:00:00')))),
            '');
        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;
