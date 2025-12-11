-- 第三方登录绑定表
CREATE TABLE IF NOT EXISTS `sys_social_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '系统用户ID',
  `source` varchar(32) NOT NULL COMMENT '第三方平台标识',
  `uuid` varchar(128) NOT NULL COMMENT '第三方用户唯一标识（openId/uuid）',
  `union_id` varchar(128) DEFAULT NULL COMMENT '第三方 unionId（如有）',
  `username` varchar(100) DEFAULT NULL COMMENT '第三方用户名/昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `access_token` varchar(512) DEFAULT NULL COMMENT '访问令牌',
  `refresh_token` varchar(512) DEFAULT NULL COMMENT '刷新令牌',
  `expire_at` datetime DEFAULT NULL COMMENT '访问令牌过期时间',
  `raw_user_info` text COMMENT '第三方原始用户信息(JSON)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_source_uuid` (`source`,`uuid`),
  KEY `idx_source_union` (`source`,`union_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方登录绑定表';

