-- Flyway 迁移脚本
-- 版本: V11
-- 描述: 为需求表添加父需求ID字段，支持需求拆分

-- 添加父需求ID字段
ALTER TABLE `requirement` 
ADD COLUMN `parent_requirement_id` bigint(20) DEFAULT NULL COMMENT '父需求ID（用于需求拆分）' AFTER `requirement_id`,
ADD KEY `idx_parent_requirement_id` (`parent_requirement_id`),
ADD CONSTRAINT `fk_requirement_parent` FOREIGN KEY (`parent_requirement_id`) REFERENCES `requirement` (`requirement_id`) ON DELETE SET NULL;

