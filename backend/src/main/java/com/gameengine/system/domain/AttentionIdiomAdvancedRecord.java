package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 成语进阶记忆游戏记录对象 attention_idiom_advanced_record
 */
@TableName("attention_idiom_advanced_record")
public class AttentionIdiomAdvancedRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String username;
    
    /** 生成提示词或主题 */
    private String prompt;
    
    /** 关联的成语ID列表（逗号分隔） */
    private String idiomIds;
    
    /** 成语文本列表(JSON字符串) */
    private String idiomList;
    
    private Integer correctCount;
    
    private Integer totalCount;
    
    /** 正确率，0-100 */
    private Integer accuracy;
    
    /** 复现耗时（秒） */
    private Integer durationSeconds;
    
    /** 答题详情(JSON字符串) */
    private String detail;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    public String getIdiomIds() {
        return idiomIds;
    }
    
    public void setIdiomIds(String idiomIds) {
        this.idiomIds = idiomIds;
    }
    
    public String getIdiomList() {
        return idiomList;
    }
    
    public void setIdiomList(String idiomList) {
        this.idiomList = idiomList;
    }
    
    public Integer getCorrectCount() {
        return correctCount;
    }
    
    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }
    
    public Integer getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    
    public Integer getAccuracy() {
        return accuracy;
    }
    
    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }
    
    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
    
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}


