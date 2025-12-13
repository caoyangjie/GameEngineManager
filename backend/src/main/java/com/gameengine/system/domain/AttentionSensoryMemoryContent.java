package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 感官记忆训练内容对象 attention_sensory_memory_content
 * 
 * @author GameEngine
 */
@TableName("attention_sensory_memory_content")
public class AttentionSensoryMemoryContent {
    
    private static final long serialVersionUID = 1L;
    
    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 内容类型：word(词语) / sentence(句子) */
    private String contentType;
    
    /** 待记忆的词语或句子 */
    private String content;
    
    /** 内容描述 */
    private String description;
    
    /** 视觉资源URL（图片/视频） */
    private String visualUrl;
    
    /** 听觉资源URL（音乐/自然声音） */
    private String audioUrl;
    
    /** 嗅觉描述（如：花香、咖啡香等） */
    private String scentDescription;
    
    /** 触觉描述（如：石头、布料等） */
    private String touchDescription;
    
    /** 味觉描述（如：水果、糖果等） */
    private String tasteDescription;
    
    /** 标签（逗号分隔） */
    private String tags;
    
    /** 创建人（用户ID） */
    private Long createdBy;
    
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
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVisualUrl() {
        return visualUrl;
    }
    
    public void setVisualUrl(String visualUrl) {
        this.visualUrl = visualUrl;
    }
    
    public String getAudioUrl() {
        return audioUrl;
    }
    
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    
    public String getScentDescription() {
        return scentDescription;
    }
    
    public void setScentDescription(String scentDescription) {
        this.scentDescription = scentDescription;
    }
    
    public String getTouchDescription() {
        return touchDescription;
    }
    
    public void setTouchDescription(String touchDescription) {
        this.touchDescription = touchDescription;
    }
    
    public String getTasteDescription() {
        return tasteDescription;
    }
    
    public void setTasteDescription(String tasteDescription) {
        this.tasteDescription = tasteDescription;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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

