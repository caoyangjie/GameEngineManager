-- flyway db modify 
ALTER TABLE attention_poetry_info MODIFY COLUMN pinyin TEXT DEFAULT NULL COMMENT '拼音';