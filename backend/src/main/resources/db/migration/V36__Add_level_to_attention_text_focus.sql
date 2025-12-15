ALTER TABLE attention_text_focus_content
    ADD COLUMN level VARCHAR(8) DEFAULT '中' COMMENT '难度等级：初/中/高/困' AFTER source_model;

