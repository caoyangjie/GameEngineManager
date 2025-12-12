-- 添加视频地址字段
ALTER TABLE `attention_idiom_info`
  ADD COLUMN `video_url` VARCHAR(512) DEFAULT NULL COMMENT '介绍成语的视频地址' AFTER `usage_text`;

