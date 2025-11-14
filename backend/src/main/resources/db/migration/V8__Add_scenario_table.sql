-- Flyway 迁移脚本
-- 版本: V8
-- 描述: 添加用户场景表

-- 用户场景表
CREATE TABLE IF NOT EXISTS `scenario` (
  `scenario_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '场景ID',
  `persona_id` bigint(20) NOT NULL COMMENT '用户画像ID',
  `canvas_id` bigint(20) NOT NULL COMMENT '画布ID',
  `title` varchar(200) DEFAULT NULL COMMENT '场景标题',
  `user_role` text COMMENT '用户角色',
  `environment` text COMMENT '环境',
  `goal` text COMMENT '目标',
  `motivation` text COMMENT '动机',
  `situation` text COMMENT '情境状况',
  `behavior_flow` text COMMENT '行为流程',
  `obstacles` text COMMENT '障碍与痛点',
  `result_expectation` text COMMENT '结果与期望',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`scenario_id`),
  KEY `idx_persona_id` (`persona_id`),
  KEY `idx_canvas_id` (`canvas_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_scenario_persona` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`persona_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_scenario_canvas` FOREIGN KEY (`canvas_id`) REFERENCES `business_model_canvas` (`canvas_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户场景表';

