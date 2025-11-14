-- Flyway 迁移脚本
-- 版本: V9
-- 描述: 添加用户需求表

-- 用户需求表
CREATE TABLE IF NOT EXISTS `requirement` (
  `requirement_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '需求ID',
  `persona_id` bigint(20) NOT NULL COMMENT '用户画像ID',
  `canvas_id` bigint(20) NOT NULL COMMENT '画布ID',
  `title` varchar(200) DEFAULT NULL COMMENT '需求标题',
  `explicit_requirement` text COMMENT '显性需求',
  `implicit_requirement` text COMMENT '隐性需求',
  `requirement_list` text COMMENT '需求列表',
  `priority` varchar(20) DEFAULT NULL COMMENT '需求优先级（Must:必须有 Should:应该有 Could:可以有 Won\'t:不会有）',
  `user_pain_points` text COMMENT '用户痛点',
  `requirement_context` text COMMENT '需求的背景和情境',
  `requirement_analysis` text COMMENT '需求分析内容',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`requirement_id`),
  KEY `idx_persona_id` (`persona_id`),
  KEY `idx_canvas_id` (`canvas_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_requirement_persona` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`persona_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_requirement_canvas` FOREIGN KEY (`canvas_id`) REFERENCES `business_model_canvas` (`canvas_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户需求表';

