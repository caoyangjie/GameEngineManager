-- 为文字专注力训练内容表增加音频地址字段
ALTER TABLE attention_text_focus_content
    ADD COLUMN audio_url VARCHAR(512) NULL COMMENT '题目语音音频地址' AFTER questions_json;


