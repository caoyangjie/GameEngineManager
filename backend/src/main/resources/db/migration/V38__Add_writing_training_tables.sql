-- Flyway 迁移脚本
-- 版本: V38
-- 描述: 添加写作训练相关表（题目、训练记录、出题错误日志）

-- 写作训练题目表：writing_training_question
CREATE TABLE IF NOT EXISTS `writing_training_question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题目ID',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID（可为空，为空表示公共题库）',
  `module_key` VARCHAR(128) NOT NULL COMMENT '模块路由键，例如 ROUTES.WRITING_WORD_EXPANSION',
  `module_title` VARCHAR(256) DEFAULT NULL COMMENT '模块标题（冗余）',
  `difficulty` VARCHAR(32) DEFAULT NULL COMMENT '难度编码：primary_low/primary_high/middle/high',
  `content` TEXT NOT NULL COMMENT '题目内容',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_wtq_user_id` (`user_id`),
  KEY `idx_wtq_module_key` (`module_key`),
  KEY `idx_wtq_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='写作训练题目表';


-- 写作训练记录表：writing_training_record
CREATE TABLE IF NOT EXISTS `writing_training_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID，用于数据隔离',
  `module_key` VARCHAR(128) NOT NULL COMMENT '模块路由键，例如 ROUTES.WRITING_WORD_EXPANSION',
  `module_title` VARCHAR(256) DEFAULT NULL COMMENT '模块标题（冗余）',
  `difficulty` VARCHAR(32) DEFAULT NULL COMMENT '难度编码：primary_low/primary_high/middle/high',
  `question_id` BIGINT NOT NULL COMMENT '关联题目ID',
  `question_content` TEXT DEFAULT NULL COMMENT '题目内容（冗余，便于列表显示）',
  `content` MEDIUMTEXT NOT NULL COMMENT '作答内容（Markdown 文本）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_wtr_user_id` (`user_id`),
  KEY `idx_wtr_user_module` (`user_id`, `module_key`),
  KEY `idx_wtr_question_id` (`question_id`),
  KEY `idx_wtr_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='写作训练记录表';


-- 写作训练 AI 出题错误日志表：writing_training_error_log
CREATE TABLE IF NOT EXISTS `writing_training_error_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID（可为空）',
  `module_key` VARCHAR(128) DEFAULT NULL COMMENT '模块路由键',
  `module_title` VARCHAR(256) DEFAULT NULL COMMENT '模块标题',
  `difficulty` VARCHAR(32) DEFAULT NULL COMMENT '难度',
  `error_message` TEXT NOT NULL COMMENT '错误消息',
  `prompt_summary` TEXT DEFAULT NULL COMMENT '触发错误的提示词或补充信息（可选，截断后的）',
  `response_summary` TEXT DEFAULT NULL COMMENT '模型原始返回片段（可选，截断后的）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_wtel_user_id` (`user_id`),
  KEY `idx_wtel_module_key` (`module_key`),
  KEY `idx_wtel_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='写作训练 AI 出题错误日志表';
