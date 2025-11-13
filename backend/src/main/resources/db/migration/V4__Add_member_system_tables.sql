-- Flyway 迁移脚本
-- 版本: V4
-- 描述: 添加会员体系相关表

-- 用户等级表
CREATE TABLE IF NOT EXISTS `member_level` (
  `level_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `level_name` varchar(50) NOT NULL COMMENT '等级名称',
  `level_code` varchar(20) NOT NULL COMMENT '等级代码（如：BRONZE, SILVER, GOLD等）',
  `level_sort` int(4) NOT NULL DEFAULT 0 COMMENT '等级排序（数字越大等级越高）',
  `min_total_deposit` decimal(20, 8) DEFAULT 0.00000000 COMMENT '最低总充值金额（USD）',
  `min_total_investment` decimal(20, 8) DEFAULT 0.00000000 COMMENT '最低总投资金额（USD）',
  `daily_bounty_rate` decimal(10, 4) DEFAULT 0.0000 COMMENT '每日赏金比例',
  `unifi_allocation_rate` decimal(10, 4) DEFAULT 0.0000 COMMENT 'UNIFI分配比例',
  `withdrawal_fee_rate` decimal(10, 4) DEFAULT 0.0000 COMMENT '提款手续费比例',
  `level_icon` varchar(200) DEFAULT '' COMMENT '等级图标',
  `level_color` varchar(20) DEFAULT '' COMMENT '等级颜色',
  `level_desc` varchar(500) DEFAULT '' COMMENT '等级描述',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`level_id`),
  UNIQUE KEY `uk_level_code` (`level_code`),
  KEY `idx_level_sort` (`level_sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户等级表';

-- 用户会员信息表
CREATE TABLE IF NOT EXISTS `member_info` (
  `member_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `current_level_id` bigint(20) DEFAULT NULL COMMENT '当前等级ID',
  `total_deposit` decimal(20, 8) DEFAULT 0.00000000 COMMENT '总充值金额（USD）',
  `total_investment` decimal(20, 8) DEFAULT 0.00000000 COMMENT '总投资金额（USD）',
  `total_withdrawal` decimal(20, 8) DEFAULT 0.00000000 COMMENT '总提款金额（USD）',
  `total_earnings` decimal(20, 8) DEFAULT 0.00000000 COMMENT '总收益（USD）',
  `level_up_time` datetime DEFAULT NULL COMMENT '等级升级时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_current_level` (`current_level_id`),
  CONSTRAINT `fk_member_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户会员信息表';

-- 虚拟货币类型表
CREATE TABLE IF NOT EXISTS `currency_type` (
  `currency_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '货币ID',
  `currency_code` varchar(20) NOT NULL COMMENT '货币代码（USD, VT, UNIFI等）',
  `currency_name` varchar(50) NOT NULL COMMENT '货币名称',
  `currency_symbol` varchar(10) DEFAULT '' COMMENT '货币符号',
  `currency_icon` varchar(200) DEFAULT '' COMMENT '货币图标',
  `decimals` int(2) DEFAULT 8 COMMENT '小数位数',
  `is_tradable` tinyint(1) DEFAULT 1 COMMENT '是否可交易（0否 1是）',
  `exchange_rate` decimal(20, 8) DEFAULT 1.00000000 COMMENT '兑换汇率（相对于USD）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`currency_id`),
  UNIQUE KEY `uk_currency_code` (`currency_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='虚拟货币类型表';

-- 用户钱包表
CREATE TABLE IF NOT EXISTS `member_wallet` (
  `wallet_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '钱包ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `currency_code` varchar(20) NOT NULL COMMENT '货币代码',
  `balance` decimal(20, 8) DEFAULT 0.00000000 COMMENT '余额',
  `locked_balance` decimal(20, 8) DEFAULT 0.00000000 COMMENT '锁定余额',
  `total_deposit` decimal(20, 8) DEFAULT 0.00000000 COMMENT '总充值',
  `total_withdrawal` decimal(20, 8) DEFAULT 0.00000000 COMMENT '总提款',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`wallet_id`),
  UNIQUE KEY `uk_user_currency` (`user_id`, `currency_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_currency_code` (`currency_code`),
  CONSTRAINT `fk_wallet_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包表';

-- 赏金库表
CREATE TABLE IF NOT EXISTS `bounty_vault` (
  `vault_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '金库ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `currency_code` varchar(20) NOT NULL DEFAULT 'VT' COMMENT '货币代码',
  `total_bounty` decimal(20, 8) DEFAULT 0.00000000 COMMENT '赏金总额',
  `auto_add_journey` tinyint(1) DEFAULT 0 COMMENT '自动追加旅程（0否 1是）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`vault_id`),
  UNIQUE KEY `uk_user_currency` (`user_id`, `currency_code`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_bounty_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='赏金库表';

-- 主金库表
CREATE TABLE IF NOT EXISTS `main_vault` (
  `vault_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '金库ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `currency_code` varchar(20) NOT NULL DEFAULT 'VT' COMMENT '货币代码',
  `balance` decimal(20, 8) DEFAULT 0.00000000 COMMENT '余额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`vault_id`),
  UNIQUE KEY `uk_user_currency` (`user_id`, `currency_code`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_main_vault_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主金库表';

-- 交易记录表
CREATE TABLE IF NOT EXISTS `member_transaction` (
  `transaction_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `transaction_type` varchar(50) NOT NULL COMMENT '交易类型（DEPOSIT:充值 WITHDRAWAL:提款 PURCHASE:购买 HARVEST:收成 ADD:追加 EXCHANGE:兑换等）',
  `wallet_type` varchar(50) DEFAULT '' COMMENT '钱包类型（USD_WALLET:USD钱包 VT_WALLET:VT钱包 BOUNTY_VAULT:赏金库 MAIN_VAULT:主金库等）',
  `action_type` varchar(20) NOT NULL COMMENT '操作类型（RECHARGE:充值 DEDUCT:扣除）',
  `currency_code` varchar(20) NOT NULL COMMENT '货币代码',
  `amount` decimal(20, 8) NOT NULL COMMENT '交易金额',
  `previous_balance` decimal(20, 8) DEFAULT 0.00000000 COMMENT '交易前余额',
  `current_balance` decimal(20, 8) DEFAULT 0.00000000 COMMENT '交易后余额',
  `exchange_rate` decimal(20, 8) DEFAULT NULL COMMENT '汇率（如VT价格）',
  `related_transaction_id` bigint(20) DEFAULT NULL COMMENT '关联交易ID',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`transaction_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_transaction_type` (`transaction_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_time` (`user_id`, `create_time`),
  CONSTRAINT `fk_transaction_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员交易记录表';

-- 购买订单表
CREATE TABLE IF NOT EXISTS `purchase_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `from_currency` varchar(20) NOT NULL COMMENT '源货币代码',
  `to_currency` varchar(20) NOT NULL COMMENT '目标货币代码',
  `from_amount` decimal(20, 8) NOT NULL COMMENT '源货币金额',
  `to_amount` decimal(20, 8) NOT NULL COMMENT '目标货币金额',
  `exchange_rate` decimal(20, 8) NOT NULL COMMENT '汇率',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '订单状态（PENDING:待处理 COMPLETED:已完成 FAILED:失败 CANCELLED:已取消）',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购买订单表';

-- 提款申请表
CREATE TABLE IF NOT EXISTS `withdrawal_request` (
  `request_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `request_no` varchar(50) NOT NULL COMMENT '申请单号',
  `currency_code` varchar(20) NOT NULL COMMENT '货币代码',
  `amount` decimal(20, 8) NOT NULL COMMENT '提款金额',
  `fee` decimal(20, 8) DEFAULT 0.00000000 COMMENT '手续费',
  `actual_amount` decimal(20, 8) NOT NULL COMMENT '实际到账金额',
  `withdrawal_address` varchar(200) NOT NULL COMMENT '提款地址',
  `network_type` varchar(20) DEFAULT '' COMMENT '网络类型（BEP20, TRC20等）',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态（PENDING:待处理 PROCESSING:处理中 COMPLETED:已完成 FAILED:失败 REJECTED:已拒绝）',
  `process_time` datetime DEFAULT NULL COMMENT '处理时间',
  `process_remark` varchar(500) DEFAULT '' COMMENT '处理备注',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`request_id`),
  UNIQUE KEY `uk_request_no` (`request_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_withdrawal_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提款申请表';

-- 插入默认等级数据
INSERT INTO `member_level` (`level_name`, `level_code`, `level_sort`, `min_total_deposit`, `min_total_investment`, `daily_bounty_rate`, `unifi_allocation_rate`, `withdrawal_fee_rate`, `level_icon`, `level_color`, `level_desc`, `status`, `create_time`, `update_time`) VALUES
('青铜会员', 'BRONZE', 1, 0.00000000, 0.00000000, 0.0100, 0.0050, 0.0500, '🥉', '#CD7F32', '初始等级，享受基础权益', '0', NOW(), NOW()),
('白银会员', 'SILVER', 2, 100.00000000, 50.00000000, 0.0150, 0.0100, 0.0400, '🥈', '#C0C0C0', '充值100 USD即可升级', '0', NOW(), NOW()),
('黄金会员', 'GOLD', 3, 500.00000000, 250.00000000, 0.0200, 0.0150, 0.0300, '🥇', '#FFD700', '充值500 USD即可升级', '0', NOW(), NOW()),
('白金会员', 'PLATINUM', 4, 2000.00000000, 1000.00000000, 0.0250, 0.0200, 0.0200, '💎', '#E5E4E2', '充值2000 USD即可升级', '0', NOW(), NOW()),
('钻石会员', 'DIAMOND', 5, 10000.00000000, 5000.00000000, 0.0300, 0.0250, 0.0100, '💠', '#B9F2FF', '充值10000 USD即可升级', '0', NOW(), NOW())
ON DUPLICATE KEY UPDATE `level_name` = VALUES(`level_name`);

-- 插入默认货币类型数据
INSERT INTO `currency_type` (`currency_code`, `currency_name`, `currency_symbol`, `currency_icon`, `decimals`, `is_tradable`, `exchange_rate`, `status`, `create_time`, `update_time`) VALUES
('USD', '美元', '$', '💰', 2, 1, 1.00000000, '0', NOW(), NOW()),
('VT', 'VT币', 'VT', 'V', 3, 1, 1.02000000, '0', NOW(), NOW()),
('UNIFI', 'UNIFI币', 'UNIFI', 'U', 8, 1, 0.00000000, '0', NOW(), NOW())
ON DUPLICATE KEY UPDATE `currency_name` = VALUES(`currency_name`);

