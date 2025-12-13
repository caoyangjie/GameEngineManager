package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数字传真训练记录对象 attention_number_fax_record
 * 
 * @author GameEngine
 */
@TableName("attention_number_fax_record")
public class AttentionNumberFaxRecord {
    
    private static final long serialVersionUID = 1L;
    
    /** 记录ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 用户ID，用于数据隔离 */
    private Long userId;
    
    /** 用户名（显示名） */
    private String username;
    
    /** 目标数字 */
    private Integer targetNumber;
    
    /** 数字组数 */
    private Integer groupCount;
    
    /** 所有数字组（JSON字符串） */
    private String groups;
    
    /** 语音包下载地址 */
    private String audioUrl;
    
    /** 正确答案 */
    private Integer correctAnswer;
    
    /** 用户答案 */
    private Integer userAnswer;
    
    /** 是否回答正确 */
    private Boolean isCorrect;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新时间 */
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
    
    public Integer getTargetNumber() {
        return targetNumber;
    }
    
    public void setTargetNumber(Integer targetNumber) {
        this.targetNumber = targetNumber;
    }
    
    public Integer getGroupCount() {
        return groupCount;
    }
    
    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }
    
    public String getGroups() {
        return groups;
    }
    
    public void setGroups(String groups) {
        this.groups = groups;
    }
    
    public String getAudioUrl() {
        return audioUrl;
    }
    
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    
    public Integer getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    public Integer getUserAnswer() {
        return userAnswer;
    }
    
    public void setUserAnswer(Integer userAnswer) {
        this.userAnswer = userAnswer;
    }
    
    public Boolean getIsCorrect() {
        return isCorrect;
    }
    
    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
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

