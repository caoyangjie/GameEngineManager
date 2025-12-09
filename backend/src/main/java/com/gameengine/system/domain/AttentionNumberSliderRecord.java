package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数字华容道游戏记录对象 attention_number_slider_record
 * 
 * @author GameEngine
 */
@TableName("attention_number_slider_record")
public class AttentionNumberSliderRecord {
    
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
    
    /** 移动次数 */
    private Integer moveCount;
    
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
    
    public Integer getMoveCount() {
        return moveCount;
    }
    
    public void setMoveCount(Integer moveCount) {
        this.moveCount = moveCount;
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

