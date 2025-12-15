package com.gameengine.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gameengine.common.core.domain.BaseEntity;

/**
 * 教育学习方法论模板对象 learning_methodology_template
 * 
 * @author GameEngine
 */
@TableName("learning_methodology_template")
public class LearningMethodologyTemplate extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 模板ID */
    @TableId(type = IdType.AUTO)
    private Long templateId;
    
    /** 模板标题 */
    private String title;
    
    /** 模板描述 */
    private String description;
    
    /** 步骤模板JSON（存储步骤配置） */
    private String stepsJson;
    
    /** 状态（0正常 1停用） */
    private String status;
    
    public Long getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStepsJson() {
        return stepsJson;
    }
    
    public void setStepsJson(String stepsJson) {
        this.stepsJson = stepsJson;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}

