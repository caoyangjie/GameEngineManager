-- Flyway 迁移脚本
-- 版本: V6
-- 描述: 为商业模式画布表添加版本字段

-- 添加版本字段
ALTER TABLE `business_model_canvas` 
ADD COLUMN `version` varchar(50) DEFAULT NULL COMMENT '版本' AFTER `title`;

