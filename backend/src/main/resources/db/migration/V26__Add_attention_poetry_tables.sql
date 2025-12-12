-- Flyway 迁移脚本
-- 版本: V26
-- 描述: 添加诗词挑战记忆训练相关表

-- 诗词资料表：存储丰富的记忆维度信息
CREATE TABLE IF NOT EXISTS `attention_poetry_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(128) NOT NULL COMMENT '诗词标题',
  `author` VARCHAR(64) DEFAULT NULL COMMENT '作者',
  `dynasty` VARCHAR(64) DEFAULT NULL COMMENT '朝代',
  `content` TEXT COMMENT '诗词内容（全文）',
  `pinyin` VARCHAR(512) DEFAULT NULL COMMENT '拼音标注',
  `poetry_type` VARCHAR(64) DEFAULT NULL COMMENT '诗词类型（如：五言绝句、七言律诗等）',
  `meaning` TEXT COMMENT '释义/译文',
  `background` TEXT COMMENT '创作背景',
  `appreciation` TEXT COMMENT '诗词赏析',
  `theme` VARCHAR(256) DEFAULT NULL COMMENT '主题/情感',
  `keywords` VARCHAR(512) DEFAULT NULL COMMENT '关键词/意象',
  `memory_cues` TEXT COMMENT '记忆线索/桩子',
  `usage_text` TEXT COMMENT '使用场景/例句（列名使用 usage_text 以规避保留字）',
  `video_url` VARCHAR(512) DEFAULT NULL COMMENT '介绍诗词的视频地址',
  `tags` VARCHAR(256) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `created_by` BIGINT DEFAULT NULL COMMENT '创建人（用户ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_poetry_title` (`title`),
  KEY `idx_created_by` (`created_by`),
  KEY `idx_tags` (`tags`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='诗词记忆资料表';

-- 诗词挑战记忆游戏记录表
CREATE TABLE IF NOT EXISTS `attention_poetry_challenge_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `username` VARCHAR(128) DEFAULT NULL COMMENT '用户名/昵称',
  `prompt` VARCHAR(512) DEFAULT NULL COMMENT '生成提示词或主题',
  `poetry_ids` VARCHAR(512) DEFAULT NULL COMMENT '关联的诗词ID列表（逗号分隔）',
  `poetry_list` TEXT COMMENT '诗词文本列表(JSON字符串)',
  `correct_count` INT DEFAULT 0 COMMENT '答对数量',
  `total_count` INT DEFAULT 0 COMMENT '题目总数',
  `accuracy` INT DEFAULT 0 COMMENT '正确率百分比（0-100）',
  `duration_seconds` INT DEFAULT 0 COMMENT '复现耗时（秒）',
  `detail` TEXT COMMENT '答题详情(JSON字符串)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_accuracy` (`accuracy`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='诗词挑战记忆游戏记录表';

