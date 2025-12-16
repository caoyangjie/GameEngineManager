-- 添加是否示例答案标记到写作训练记录表
ALTER TABLE `writing_training_record`
  ADD COLUMN `is_example` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否示例答案：1-示例，0-用户作答' AFTER `content`;
