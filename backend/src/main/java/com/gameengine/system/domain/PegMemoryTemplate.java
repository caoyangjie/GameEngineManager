package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 定桩记忆模板对象 attention_peg_memory_template
 */
@TableName("attention_peg_memory_template")
public class PegMemoryTemplate {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 模板名称 */
    private String templateName;
    
    /** 模板描述 */
    private String description;
    
    /** 定桩项列表(JSON字符串) */
    private String pegItems;
    
    /** 定桩总数 */
    private Integer totalPegs;
    
    /** 分类（如：数字桩、地点桩、身体桩等） */
    private String category;
    
    /** 标签（逗号分隔） */
    private String tags;
    
    /** 是否默认模板（0-否，1-是） */
    private Integer isDefault;
    
    /** 创建人 */
    private Long createdBy;
    
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
    
    public String getTemplateName() {
        return templateName;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPegItems() {
        return pegItems;
    }
    
    public void setPegItems(String pegItems) {
        this.pegItems = pegItems;
    }
    
    public Integer getTotalPegs() {
        return totalPegs;
    }
    
    public void setTotalPegs(Integer totalPegs) {
        this.totalPegs = totalPegs;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public Integer getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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

