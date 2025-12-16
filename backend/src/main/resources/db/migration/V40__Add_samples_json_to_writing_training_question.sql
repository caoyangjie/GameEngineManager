-- 为写作训练题目表增加示例答案 JSON 字段
ALTER TABLE `writing_training_question`
  ADD COLUMN `samples_json` MEDIUMTEXT NULL COMMENT '示例答案 JSON 数组，方便快速预览示例' AFTER `content`;
