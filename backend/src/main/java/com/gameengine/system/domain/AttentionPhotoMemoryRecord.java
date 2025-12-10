package com.gameengine.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 照相记忆游戏记录对象 attention_photo_memory_record
 * 
 * @author GameEngine
 */
@TableName("attention_photo_memory_record")
public class AttentionPhotoMemoryRecord {
    
    private static final long serialVersionUID = 1L;
    
    /** 记录ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 用户ID，用于数据隔离 */
    private Long userId;
    
    /** 用户名（显示名） */
    private String username;
    
    /** 方格尺寸（如 4 表示 4x4） */
    private Integer gridSize;
    
    /** 完成所用毫秒数 */
    private Long durationMs;
    
    /** 准确率（0-100） */
    private BigDecimal accuracy;
    
    /** 总配对数 */
    private Integer totalPairs;
    
    /** 成功匹配次数 */
    private Integer successfulMatches;
    
    /** 总尝试次数 */
    private Integer totalAttempts;
    
    /** 总格子数（兼容字段） */
    private Integer totalCells;
    
    /** 正确点击的格子数（兼容字段） */
    private Integer correctCells;
    
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
    
    public Integer getGridSize() {
        return gridSize;
    }
    
    public void setGridSize(Integer gridSize) {
        this.gridSize = gridSize;
    }
    
    public Long getDurationMs() {
        return durationMs;
    }
    
    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }
    
    public BigDecimal getAccuracy() {
        return accuracy;
    }
    
    public void setAccuracy(BigDecimal accuracy) {
        this.accuracy = accuracy;
    }
    
    public Integer getTotalPairs() {
        return totalPairs;
    }
    
    public void setTotalPairs(Integer totalPairs) {
        this.totalPairs = totalPairs;
    }
    
    public Integer getSuccessfulMatches() {
        return successfulMatches;
    }
    
    public void setSuccessfulMatches(Integer successfulMatches) {
        this.successfulMatches = successfulMatches;
    }
    
    public Integer getTotalAttempts() {
        return totalAttempts;
    }
    
    public void setTotalAttempts(Integer totalAttempts) {
        this.totalAttempts = totalAttempts;
    }
    
    public Integer getTotalCells() {
        return totalCells;
    }
    
    public void setTotalCells(Integer totalCells) {
        this.totalCells = totalCells;
    }
    
    public Integer getCorrectCells() {
        return correctCells;
    }
    
    public void setCorrectCells(Integer correctCells) {
        this.correctCells = correctCells;
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

