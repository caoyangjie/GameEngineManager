-- Flyway 迁移脚本
-- 版本: V2
-- 描述: 添加用户分类字段和用户扩展表

-- 为用户表添加分类字段
ALTER TABLE `sys_user` 
ADD COLUMN `user_category` varchar(50) DEFAULT 'player' COMMENT '用户分类（player:玩家 recruiter:招聘者 admin:管理员）' 
AFTER `nick_name`;

-- 用户扩展表
CREATE TABLE IF NOT EXISTS `sys_user_ext` (
  `ext_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '扩展ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `recruitment_link` varchar(500) DEFAULT '' COMMENT '招聘链接',
  `current_level` varchar(50) DEFAULT '' COMMENT '当前旅程等级',
  `player_id` varchar(50) DEFAULT '' COMMENT '玩家ID',
  `bep20_address` varchar(200) DEFAULT '' COMMENT 'BEP20地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ext_id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户扩展信息表';

