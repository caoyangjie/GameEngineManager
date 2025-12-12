-- 成语资料表：存储丰富的记忆维度信息
CREATE TABLE IF NOT EXISTS `attention_idiom_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `idiom` VARCHAR(64) NOT NULL COMMENT '成语本身',
  `pinyin` VARCHAR(128) DEFAULT NULL COMMENT '拼音',
  `literal_meaning` VARCHAR(256) DEFAULT NULL COMMENT '字面意思',
  `meaning` VARCHAR(512) DEFAULT NULL COMMENT '释义/引申义',
  `moral` VARCHAR(512) DEFAULT NULL COMMENT '寓意/启示',
  `origin_dynasty` VARCHAR(64) DEFAULT NULL COMMENT '产生朝代',
  `origin_source` VARCHAR(256) DEFAULT NULL COMMENT '出处/典籍',
  `background` TEXT COMMENT '产生背景',
  `story` TEXT COMMENT '背景故事',
  `protagonist` VARCHAR(256) DEFAULT NULL COMMENT '主人公/主角',
  `related_persons` VARCHAR(512) DEFAULT NULL COMMENT '关联人物列表',
  `usage` TEXT COMMENT '使用场景/例句',
  `memory_cues` TEXT COMMENT '记忆桩/联想线索',
  `tags` VARCHAR(256) DEFAULT NULL COMMENT '标签列表',
  `created_by` BIGINT DEFAULT NULL COMMENT '创建人（用户ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_idiom_text` (`idiom`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成语记忆资料表';

-- 成语进阶记忆游戏记录表
CREATE TABLE IF NOT EXISTS `attention_idiom_advanced_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `username` VARCHAR(128) DEFAULT NULL COMMENT '用户名/昵称',
  `prompt` VARCHAR(512) DEFAULT NULL COMMENT '生成使用的提示词',
  `idiom_ids` VARCHAR(512) DEFAULT NULL COMMENT '参与训练的成语ID列表，逗号分隔',
  `idiom_list` TEXT COMMENT '参与训练的成语文本列表(JSON)',
  `correct_count` INT DEFAULT 0 COMMENT '答对数量',
  `total_count` INT DEFAULT 0 COMMENT '题目总数',
  `accuracy` INT DEFAULT 0 COMMENT '正确率百分比',
  `duration_seconds` INT DEFAULT 0 COMMENT '复现用时（秒）',
  `detail` TEXT COMMENT '答题详情(JSON)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成语进阶记忆游戏记录表';


