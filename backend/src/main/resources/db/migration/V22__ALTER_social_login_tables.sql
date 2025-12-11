-- Flyway 迁移脚本
-- 版本: V22
-- 描述: 修改第三方登录绑定表的头像地址字段长度
ALTER TABLE sys_social_user MODIFY COLUMN avatar varchar(2000) DEFAULT NULL COMMENT '头像地址';

-- 用户表添加头像字段
ALTER TABLE sys_user ADD COLUMN avatar varchar(2000) DEFAULT NULL COMMENT '头像地址';

-- 用户扩展表添加头像字段
ALTER TABLE sys_user_ext ADD COLUMN avatar varchar(2000) DEFAULT NULL COMMENT '头像地址';