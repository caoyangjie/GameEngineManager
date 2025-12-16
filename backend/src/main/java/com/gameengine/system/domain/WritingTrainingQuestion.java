package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 写作训练题目对象 writing_training_question
 *
 * 题目由 DeepSeek 生成并保存，用于后续随机抽取与训练记录关联。
 */
@TableName("writing_training_question")
public class WritingTrainingQuestion {

    /** 题目ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID（可选，用于按用户隔离题库；为 null 时表示公共题库） */
    private Long userId;

    /** 模块路由键，例如 ROUTES.WRITING_WORD_EXPANSION */
    private String moduleKey;

    /** 模块标题（冗余） */
    private String moduleTitle;

    /** 难度编码：primary_low/primary_high/middle/high */
    private String difficulty;

    /** 题目内容 */
    private String content;

    /**
     * 示例答案 JSON 数组
     *
     * 结构示例：
     * [
     *   "示例答案1",
     *   "示例答案2"
     * ]
     */
    private String samplesJson;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSamplesJson() {
        return samplesJson;
    }

    public void setSamplesJson(String samplesJson) {
        this.samplesJson = samplesJson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
