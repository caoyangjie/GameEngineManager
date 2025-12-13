-- Flyway 迁移脚本
-- 版本: V30
-- 描述: 添加定桩记忆法训练相关表

-- 定桩记忆模板表：存储数字与记忆目标的对应关系
CREATE TABLE IF NOT EXISTS `attention_peg_memory_template` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` VARCHAR(128) NOT NULL COMMENT '模板名称',
  `description` TEXT COMMENT '模板描述',
  `peg_items` TEXT NOT NULL COMMENT '定桩项列表(JSON字符串)，格式：[{"number": 1, "target": "记忆目标1", "imageUrl": "图片URL(可选)"}, ...]',
  `total_pegs` INT DEFAULT 0 COMMENT '定桩总数',
  `category` VARCHAR(64) DEFAULT NULL COMMENT '分类（如：数字桩、地点桩、身体桩等）',
  `tags` VARCHAR(256) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否默认模板（0-否，1-是）',
  `created_by` BIGINT DEFAULT NULL COMMENT '创建人（用户ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_created_by` (`created_by`),
  KEY `idx_category` (`category`),
  KEY `idx_is_default` (`is_default`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定桩记忆模板表';

-- 定桩记忆训练记录表
CREATE TABLE IF NOT EXISTS `attention_peg_memory_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `username` VARCHAR(128) DEFAULT NULL COMMENT '用户名/昵称',
  `template_id` BIGINT DEFAULT NULL COMMENT '使用的模板ID',
  `template_name` VARCHAR(128) DEFAULT NULL COMMENT '模板名称',
  `training_mode` VARCHAR(32) DEFAULT 'number_to_target' COMMENT '训练模式：number_to_target(数字→目标), target_to_number(目标→数字)',
  `peg_count` INT DEFAULT 0 COMMENT '训练的定桩数量',
  `correct_count` INT DEFAULT 0 COMMENT '答对数量',
  `total_count` INT DEFAULT 0 COMMENT '题目总数',
  `accuracy` INT DEFAULT 0 COMMENT '正确率百分比（0-100）',
  `duration_seconds` INT DEFAULT 0 COMMENT '训练耗时（秒）',
  `detail` TEXT COMMENT '答题详情(JSON字符串)',
  `difficulty` VARCHAR(32) DEFAULT NULL COMMENT '难度级别',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_accuracy` (`accuracy`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定桩记忆训练记录表';

