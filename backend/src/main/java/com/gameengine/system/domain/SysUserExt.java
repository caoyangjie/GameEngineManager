package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户扩展对象 sys_user_ext
 * 
 * @author GameEngine
 */
@TableName("sys_user_ext")
public class SysUserExt {
    
    private static final long serialVersionUID = 1L;
    
    /** 扩展ID */
    @TableId(type = IdType.AUTO)
    private Long extId;
    
    /** 用户ID */
    private Long userId;
    
    /** 招聘链接 */
    private String recruitmentLink;
    
    /** 当前旅程等级 */
    private String currentLevel;
    
    /** 玩家ID */
    private String playerId;
    
    /** BEP20地址 */
    private String bep20Address;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    public Long getExtId() {
        return extId;
    }
    
    public void setExtId(Long extId) {
        this.extId = extId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getRecruitmentLink() {
        return recruitmentLink;
    }
    
    public void setRecruitmentLink(String recruitmentLink) {
        this.recruitmentLink = recruitmentLink;
    }
    
    public String getCurrentLevel() {
        return currentLevel;
    }
    
    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }
    
    public String getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    
    public String getBep20Address() {
        return bep20Address;
    }
    
    public void setBep20Address(String bep20Address) {
        this.bep20Address = bep20Address;
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

