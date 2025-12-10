-- Flyway 迁移脚本
-- 版本: V20
-- 描述: 添加照相记忆游戏记录表

-- 照相记忆游戏记录表
CREATE TABLE IF NOT EXISTS `attention_photo_memory_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID，用于数据隔离',
  `username` varchar(50) NOT NULL COMMENT '用户名（显示名）',
  `grid_size` int(11) NOT NULL COMMENT '方格尺寸（如 4 表示 4x4）',
  `duration_ms` bigint(20) NOT NULL COMMENT '完成所用毫秒数',
  `accuracy` decimal(5,2) NOT NULL COMMENT '准确率（0-100）',
  `total_pairs` int(11) DEFAULT NULL COMMENT '总配对数',
  `successful_matches` int(11) DEFAULT NULL COMMENT '成功匹配次数',
  `total_attempts` int(11) DEFAULT NULL COMMENT '总尝试次数',
  `total_cells` int(11) DEFAULT NULL COMMENT '总格子数（兼容字段）',
  `correct_cells` int(11) DEFAULT NULL COMMENT '正确点击的格子数（兼容字段）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_accuracy` (`accuracy`),
  KEY `idx_grid_size` (`grid_size`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='照相记忆游戏记录表';

