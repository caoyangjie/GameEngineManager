-- Flyway 迁移脚本
-- 版本: V29
-- 描述: 添加感官探险记忆法训练相关表

-- 感官记忆训练内容表：存储待记忆的词语/句子及其感官体验配置
CREATE TABLE IF NOT EXISTS `attention_sensory_memory_content` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content_type` VARCHAR(32) NOT NULL DEFAULT 'word' COMMENT '内容类型：word(词语) / sentence(句子)',
  `content` VARCHAR(512) NOT NULL COMMENT '待记忆的词语或句子',
  `description` TEXT COMMENT '内容描述',
  `visual_url` VARCHAR(512) DEFAULT NULL COMMENT '视觉资源URL（图片/视频）',
  `audio_url` VARCHAR(512) DEFAULT NULL COMMENT '听觉资源URL（音乐/自然声音）',
  `scent_description` VARCHAR(256) DEFAULT NULL COMMENT '嗅觉描述（如：花香、咖啡香等）',
  `touch_description` VARCHAR(256) DEFAULT NULL COMMENT '触觉描述（如：石头、布料等）',
  `taste_description` VARCHAR(256) DEFAULT NULL COMMENT '味觉描述（如：水果、糖果等）',
  `tags` VARCHAR(256) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `created_by` BIGINT DEFAULT NULL COMMENT '创建人（用户ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_created_by` (`created_by`),
  KEY `idx_content_type` (`content_type`),
  KEY `idx_tags` (`tags`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='感官记忆训练内容表';

-- 感官记忆训练记录表
CREATE TABLE IF NOT EXISTS `attention_sensory_memory_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `username` VARCHAR(128) DEFAULT NULL COMMENT '用户名/昵称',
  `content_ids` VARCHAR(512) DEFAULT NULL COMMENT '关联的内容ID列表（逗号分隔）',
  `content_list` TEXT COMMENT '训练内容列表(JSON字符串)',
  `content_count` INT DEFAULT 0 COMMENT '训练内容数量',
  `memorize_duration_seconds` INT DEFAULT 0 COMMENT '记忆阶段耗时（秒）',
  `recall_duration_seconds` INT DEFAULT 0 COMMENT '回忆阶段耗时（秒）',
  `total_duration_seconds` INT DEFAULT 0 COMMENT '总耗时（秒）',
  `correct_count` INT DEFAULT 0 COMMENT '回忆正确数量',
  `accuracy` INT DEFAULT 0 COMMENT '正确率百分比（0-100）',
  `sensory_experiences` TEXT COMMENT '感官体验记录(JSON字符串)',
  `recall_result` TEXT COMMENT '回忆结果详情(JSON字符串)',
  `user_feedback` TEXT COMMENT '用户反馈/分享',
  `difficulty` VARCHAR(32) DEFAULT NULL COMMENT '难度级别',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_accuracy` (`accuracy`),
  KEY `idx_difficulty` (`difficulty`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='感官探险记忆法训练记录表';

