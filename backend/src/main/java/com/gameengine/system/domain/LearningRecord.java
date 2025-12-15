package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 学习记录对象 learning_record
 * 
 * @author GameEngine
 */
@TableName("learning_record")
public class LearningRecord {
    
    private static final long serialVersionUID = 1L;
    
    /** 记录ID */
    @TableId(type = IdType.AUTO)
    private Long recordId;
    
    /** 用户ID，用于数据隔离 */
    private Long userId;
    
    /** 模板ID */
    private Long templateId;
    
    /** 模板标题（冗余字段，方便查询） */
    private String templateTitle;
    
    /** 学生姓名 */
    private String studentName;
    
    /** 学习主题 */
    private String learningTopic;
    
    /** 步骤数据（JSON格式，包含步骤、任务完成状态、笔记、任务记录等） */
    private String stepsData;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    public Long getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    
    public String getTemplateTitle() {
        return templateTitle;
    }
    
    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getLearningTopic() {
        return learningTopic;
    }
    
    public void setLearningTopic(String learningTopic) {
        this.learningTopic = learningTopic;
    }
    
    public String getStepsData() {
        return stepsData;
    }
    
    public void setStepsData(String stepsData) {
        this.stepsData = stepsData;
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

