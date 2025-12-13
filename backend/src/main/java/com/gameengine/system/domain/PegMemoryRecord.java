package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 定桩记忆训练记录对象 attention_peg_memory_record
 */
@TableName("attention_peg_memory_record")
public class PegMemoryRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String username;
    
    /** 使用的模板ID */
    private Long templateId;
    
    /** 模板名称 */
    private String templateName;
    
    /** 训练模式：number_to_target(数字→目标), target_to_number(目标→数字) */
    private String trainingMode;
    
    /** 训练的定桩数量 */
    private Integer pegCount;
    
    private Integer correctCount;
    
    private Integer totalCount;
    
    /** 正确率，0-100 */
    private Integer accuracy;
    
    /** 训练耗时（秒） */
    private Integer durationSeconds;
    
    /** 答题详情(JSON字符串) */
    private String detail;
    
    /** 难度级别 */
    private String difficulty;
    
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
    
    public Long getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public String getTrainingMode() {
        return trainingMode;
    }
    
    public void setTrainingMode(String trainingMode) {
        this.trainingMode = trainingMode;
    }
    
    public Integer getPegCount() {
        return pegCount;
    }
    
    public void setPegCount(Integer pegCount) {
        this.pegCount = pegCount;
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
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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

