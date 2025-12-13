-- Flyway 迁移脚本
-- 版本: V28
-- 描述: 添加数字传真训练记录表

-- 数字传真训练记录表
CREATE TABLE IF NOT EXISTS `attention_number_fax_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID，用于数据隔离',
  `username` varchar(50) NOT NULL COMMENT '用户名（显示名）',
  `target_number` int(11) NOT NULL COMMENT '目标数字',
  `group_count` int(11) NOT NULL COMMENT '数字组数',
  `groups` text NOT NULL COMMENT '所有数字组（JSON字符串）',
  `audio_url` varchar(500) DEFAULT NULL COMMENT '语音包下载地址',
  `correct_answer` int(11) NOT NULL COMMENT '正确答案',
  `user_answer` int(11) DEFAULT NULL COMMENT '用户答案',
  `is_correct` tinyint(1) DEFAULT NULL COMMENT '是否回答正确',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_number` (`target_number`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数字传真训练记录表';

