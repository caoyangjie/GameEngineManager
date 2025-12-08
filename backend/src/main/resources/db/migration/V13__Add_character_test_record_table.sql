-- Flyway 迁移脚本
-- 版本: V5
-- 描述: 添加识字测试记录表

-- 识字测试记录表
CREATE TABLE IF NOT EXISTS `character_test_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '测试记录ID',
  `student_name` varchar(50) NOT NULL COMMENT '学生姓名',
  `test_date` date NOT NULL COMMENT '测试日期',
  `education_level` varchar(20) NOT NULL COMMENT '教育阶段: primary(小学), middle(初中), high(高中)',
  `test_count` int(11) NOT NULL DEFAULT 0 COMMENT '测试字数',
  `show_pinyin` tinyint(1) DEFAULT 0 COMMENT '是否显示拼音',
  `characters` text COMMENT '测试字符数据（JSON格式）',
  `correct_count` int(11) DEFAULT 0 COMMENT '正确数量',
  `incorrect_count` int(11) DEFAULT 0 COMMENT '错误数量',
  `mastered_count` int(11) DEFAULT 0 COMMENT '掌握数量',
  `mastery_rate` int(11) DEFAULT 0 COMMENT '掌握率（百分比）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_name` (`student_name`),
  KEY `idx_test_date` (`test_date`),
  KEY `idx_education_level` (`education_level`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='识字测试记录表';

