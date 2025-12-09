-- Flyway 迁移脚本
-- 版本: V15
-- 描述: 添加用户ID到识字测试记录和数学测试记录表
ALTER TABLE character_test_record ADD COLUMN user_id BIGINT NOT NULL;


ALTER TABLE math_test_record ADD COLUMN user_id BIGINT NOT NULL;