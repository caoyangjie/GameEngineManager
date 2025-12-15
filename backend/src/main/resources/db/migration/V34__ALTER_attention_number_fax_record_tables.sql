-- Flyway 迁移脚本
-- 版本: V34
-- 描述: 修改数字传真训练记录表的数字组字段长度
ALTER TABLE attention_number_fax_record CHANGE `groups` groups_json TEXT COMMENT '所有数字组（JSON字符串）';

