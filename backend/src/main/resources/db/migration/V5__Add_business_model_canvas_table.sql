-- Flyway 迁移脚本
-- 版本: V5
-- 描述: 添加商业模式画布表

-- 商业模式画布表
CREATE TABLE IF NOT EXISTS `business_model_canvas` (
  `canvas_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '画布ID',
  `title` varchar(200) NOT NULL COMMENT '画布标题',
  `key_partners` text COMMENT '关键合作',
  `key_activities` text COMMENT '关键活动',
  `key_resources` text COMMENT '关键资源',
  `value_propositions` text COMMENT '价值主张',
  `customer_relationships` text COMMENT '客户关系',
  `channels` text COMMENT '渠道通路',
  `customer_segments` text COMMENT '客户细分',
  `cost_structure` text COMMENT '成本构成',
  `revenue_streams` text COMMENT '售卖途径',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`canvas_id`),
  KEY `idx_title` (`title`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商业模式画布表';

