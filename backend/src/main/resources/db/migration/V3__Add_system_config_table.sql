-- Flyway 迁移脚本
-- 版本: V3
-- 描述: 添加系统配置表

-- 系统配置表
CREATE TABLE IF NOT EXISTS `sys_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` varchar(500) DEFAULT '' COMMENT '配置值',
  `config_type` varchar(50) DEFAULT 'string' COMMENT '配置类型（string:字符串 number:数字 boolean:布尔）',
  `config_desc` varchar(500) DEFAULT '' COMMENT '配置描述',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 插入默认配置数据
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_type`, `config_desc`, `create_time`, `update_time`) VALUES
('deposit.bep20.address', '0xCc3df0Ccdec9D6ADCD3AfD999D1282Bd1939d8cd', 'string', 'BEP20存款地址', NOW(), NOW()),
('deposit.trc20.address', 'TQfvBWeQwkvHXaaZZbdfZ6YGxENgyqqwMn', 'string', 'TRC20存款地址', NOW(), NOW())
ON DUPLICATE KEY UPDATE `config_value` = VALUES(`config_value`);

