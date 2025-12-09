-- Flyway 迁移脚本
-- 版本: V19
-- 描述: 添加数字华容道游戏记录表

-- 数字华容道游戏记录表
CREATE TABLE IF NOT EXISTS `attention_number_slider_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID，用于数据隔离',
  `username` varchar(50) NOT NULL COMMENT '用户名（显示名）',
  `grid_size` int(11) NOT NULL COMMENT '方格尺寸（如 4 表示 4x4）',
  `duration_ms` bigint(20) NOT NULL COMMENT '完成所用毫秒数',
  `move_count` int(11) NOT NULL COMMENT '移动次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_duration_ms` (`duration_ms`),
  KEY `idx_grid_size` (`grid_size`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数字华容道游戏记录表';

