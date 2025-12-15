-- Flyway 迁移脚本
-- 版本: V31
-- 描述: 添加教育学习方法论模板表

-- 教育学习方法论模板表
CREATE TABLE IF NOT EXISTS `learning_methodology_template` (
  `template_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `title` VARCHAR(200) NOT NULL COMMENT '模板标题',
  `description` TEXT COMMENT '模板描述',
  `steps_json` TEXT NOT NULL COMMENT '步骤模板JSON（存储步骤配置）',
  `status` CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`template_id`),
  KEY `idx_title` (`title`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教育学习方法论模板表';

