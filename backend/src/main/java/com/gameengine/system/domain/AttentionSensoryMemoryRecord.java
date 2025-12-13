package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 感官探险记忆法训练记录对象 attention_sensory_memory_record
 * 
 * @author GameEngine
 */
@TableName("attention_sensory_memory_record")
public class AttentionSensoryMemoryRecord {
    
    private static final long serialVersionUID = 1L;
    
    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 用户名/昵称 */
    private String username;
    
    /** 关联的内容ID列表（逗号分隔） */
    private String contentIds;
    
    /** 训练内容列表(JSON字符串) */
    private String contentList;
    
    /** 训练内容数量 */
    private Integer contentCount;
    
    /** 记忆阶段耗时（秒） */
    private Integer memorizeDurationSeconds;
    
    /** 回忆阶段耗时（秒） */
    private Integer recallDurationSeconds;
    
    /** 总耗时（秒） */
    private Integer totalDurationSeconds;
    
    /** 回忆正确数量 */
    private Integer correctCount;
    
    /** 正确率百分比（0-100） */
    private Integer accuracy;
    
    /** 感官体验记录(JSON字符串) */
    private String sensoryExperiences;
    
    /** 回忆结果详情(JSON字符串) */
    private String recallResult;
    
    /** 用户反馈/分享 */
    private String userFeedback;
    
    /** 难度级别 */
    private String difficulty;
    
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
    
    public String getContentIds() {
        return contentIds;
    }
    
    public void setContentIds(String contentIds) {
        this.contentIds = contentIds;
    }
    
    public String getContentList() {
        return contentList;
    }
    
    public void setContentList(String contentList) {
        this.contentList = contentList;
    }
    
    public Integer getContentCount() {
        return contentCount;
    }
    
    public void setContentCount(Integer contentCount) {
        this.contentCount = contentCount;
    }
    
    public Integer getMemorizeDurationSeconds() {
        return memorizeDurationSeconds;
    }
    
    public void setMemorizeDurationSeconds(Integer memorizeDurationSeconds) {
        this.memorizeDurationSeconds = memorizeDurationSeconds;
    }
    
    public Integer getRecallDurationSeconds() {
        return recallDurationSeconds;
    }
    
    public void setRecallDurationSeconds(Integer recallDurationSeconds) {
        this.recallDurationSeconds = recallDurationSeconds;
    }
    
    public Integer getTotalDurationSeconds() {
        return totalDurationSeconds;
    }
    
    public void setTotalDurationSeconds(Integer totalDurationSeconds) {
        this.totalDurationSeconds = totalDurationSeconds;
    }
    
    public Integer getCorrectCount() {
        return correctCount;
    }
    
    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }
    
    public Integer getAccuracy() {
        return accuracy;
    }
    
    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }
    
    public String getSensoryExperiences() {
        return sensoryExperiences;
    }
    
    public void setSensoryExperiences(String sensoryExperiences) {
        this.sensoryExperiences = sensoryExperiences;
    }
    
    public String getRecallResult() {
        return recallResult;
    }
    
    public void setRecallResult(String recallResult) {
        this.recallResult = recallResult;
    }
    
    public String getUserFeedback() {
        return userFeedback;
    }
    
    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
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

