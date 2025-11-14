-- Flyway 迁移脚本
-- 版本: V10
-- 描述: 为用户画像表添加需求分析字段

-- 为用户画像表添加需求分析字段
ALTER TABLE `persona` 
ADD COLUMN `requirement_analysis` text COMMENT '需求分析内容' AFTER `usage_scenario`;

