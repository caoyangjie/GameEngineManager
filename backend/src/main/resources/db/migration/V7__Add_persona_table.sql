-- Flyway 迁移脚本
-- 版本: V7
-- 描述: 添加用户画像表

-- 用户画像表
CREATE TABLE IF NOT EXISTS `persona` (
  `persona_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户画像ID',
  `canvas_id` bigint(20) NOT NULL COMMENT '画布ID',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别（male:男 female:女 other:其他）',
  `identity` varchar(200) DEFAULT NULL COMMENT '身份',
  `hobbies` text COMMENT '爱好',
  `usage_scenario` text COMMENT '使用产品的场景',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像地址',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`persona_id`),
  KEY `idx_canvas_id` (`canvas_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_persona_canvas` FOREIGN KEY (`canvas_id`) REFERENCES `business_model_canvas` (`canvas_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户画像表';

