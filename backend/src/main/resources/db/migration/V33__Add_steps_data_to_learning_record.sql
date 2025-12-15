-- Flyway 迁移脚本
-- 版本: V33
-- 描述: 为学习记录表添加步骤数据字段

-- 添加步骤数据字段（JSON格式存储步骤、任务、笔记等数据）
ALTER TABLE `learning_record` 
ADD COLUMN `steps_data` TEXT COMMENT '步骤数据（JSON格式，包含步骤、任务完成状态、笔记、任务记录等）' AFTER `learning_topic`;

