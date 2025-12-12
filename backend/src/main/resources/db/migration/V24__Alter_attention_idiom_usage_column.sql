-- 避免使用 MySQL 保留字 usage，改名为 usage_text
ALTER TABLE `attention_idiom_info`
  CHANGE COLUMN `usage` `usage_text` TEXT NULL COMMENT '使用场景/例句';


