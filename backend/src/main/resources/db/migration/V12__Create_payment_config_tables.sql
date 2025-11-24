-- Flyway 迁移脚本
-- 版本: V12
-- 描述: 添加支付配置和支付订单相关表

-- 支付配置表
CREATE TABLE IF NOT EXISTS `payment_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `payment_type` VARCHAR(50) NOT NULL COMMENT '支付类型：ALIPAY-支付宝, WECHAT-微信, BESTPAY-翼支付',
  `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
  `app_id` VARCHAR(100) DEFAULT NULL COMMENT '应用ID',
  `mch_id` VARCHAR(100) DEFAULT NULL COMMENT '商户号',
  `api_key` VARCHAR(500) DEFAULT NULL COMMENT 'API密钥',
  `private_key` TEXT DEFAULT NULL COMMENT '私钥',
  `public_key` TEXT DEFAULT NULL COMMENT '公钥',
  `alipay_public_key` TEXT DEFAULT NULL COMMENT '支付宝公钥',
  `sign_type` VARCHAR(20) DEFAULT NULL COMMENT '签名类型：RSA/RSA2',
  `p12_file_path` VARCHAR(500) DEFAULT NULL COMMENT 'P12证书文件路径（微信退款等需要）',
  `notify_url` VARCHAR(500) DEFAULT NULL COMMENT '异步通知地址',
  `return_url` VARCHAR(500) DEFAULT NULL COMMENT '同步返回地址',
  `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_payment_type` (`payment_type`),
  KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付配置表';

-- 支付订单表
CREATE TABLE IF NOT EXISTS `payment_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `out_trade_no` VARCHAR(64) NOT NULL COMMENT '商户订单号',
  `payment_type` VARCHAR(50) NOT NULL COMMENT '支付类型：ALIPAY-支付宝, WECHAT-微信, BESTPAY-翼支付',
  `trade_type` VARCHAR(50) DEFAULT NULL COMMENT '交易类型：NATIVE-扫码, JSAPI-JSAPI支付, MICROPAY-刷卡支付等',
  `subject` VARCHAR(256) DEFAULT NULL COMMENT '订单标题',
  `body` VARCHAR(500) DEFAULT NULL COMMENT '订单描述',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单金额',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '订单状态：PENDING-待支付, SUCCESS-支付成功, FAILED-支付失败, REFUNDED-已退款, CANCELLED-已取消',
  `trade_no` VARCHAR(64) DEFAULT NULL COMMENT '支付平台交易号',
  `config_id` BIGINT(20) DEFAULT NULL COMMENT '支付配置ID',
  `request_data` TEXT DEFAULT NULL COMMENT '请求数据（JSON格式）',
  `response_data` TEXT DEFAULT NULL COMMENT '响应数据（JSON格式）',
  `notify_data` TEXT DEFAULT NULL COMMENT '异步通知数据（JSON格式）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_out_trade_no` (`out_trade_no`),
  KEY `idx_payment_type` (`payment_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表';

