-- Flyway 迁移脚本
-- 版本: V32
-- 描述: 添加学习记录表

-- 学习记录表
CREATE TABLE IF NOT EXISTS `learning_record` (
  `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID，用于数据隔离',
  `template_id` BIGINT NOT NULL COMMENT '模板ID',
  `template_title` VARCHAR(200) COMMENT '模板标题（冗余字段，方便查询）',
  `student_name` VARCHAR(50) NOT NULL COMMENT '学生姓名',
  `learning_topic` VARCHAR(200) NOT NULL COMMENT '学习主题',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_student_name` (`student_name`),
  KEY `idx_learning_topic` (`learning_topic`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

