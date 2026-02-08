-- Create hello database if not exists
CREATE DATABASE IF NOT EXISTS hello_db;
USE hello_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL COMMENT '用户名',
  email VARCHAR(255) UNIQUE COMMENT '邮箱',
  phone VARCHAR(20) COMMENT '电话',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted_at TIMESTAMP NULL COMMENT '删除时间',
  INDEX idx_email (email),
  INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- Insert sample data
INSERT INTO users (name, email, phone) VALUES 
  ('张三', 'zhangsan@example.com', '13800138000'),
  ('李四', 'lisi@example.com', '13800138001'),
  ('王五', 'wangwu@example.com', '13800138002');
