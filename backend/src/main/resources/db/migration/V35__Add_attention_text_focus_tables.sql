-- 文字专注力训练表
CREATE TABLE IF NOT EXISTS attention_text_focus_content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NULL,
    username VARCHAR(64) NULL,
    theme VARCHAR(128) DEFAULT '日常生活',
    title VARCHAR(255) NULL,
    paragraph TEXT NOT NULL,
    questions_json TEXT NOT NULL,
    source_model VARCHAR(64) DEFAULT 'deepseek',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_text_focus_ctime (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文字专注力训练生成内容';

CREATE TABLE IF NOT EXISTS attention_text_focus_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NULL,
    username VARCHAR(64) NULL,
    content_id BIGINT NULL,
    content_title VARCHAR(255) NULL,
    total_questions INT NULL,
    correct_count INT NULL,
    accuracy INT NULL,
    duration_seconds INT NULL,
    answers_json TEXT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_text_focus_record_ctime (create_time),
    INDEX idx_text_focus_record_user (user_id),
    INDEX idx_text_focus_record_content (content_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文字专注力训练记录';

