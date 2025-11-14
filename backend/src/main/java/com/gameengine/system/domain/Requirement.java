package com.gameengine.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gameengine.common.core.domain.BaseEntity;

/**
 * 用户需求对象 requirement
 * 
 * @author GameEngine
 */
@TableName("requirement")
public class Requirement extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 需求ID */
    @TableId(type = IdType.AUTO)
    private Long requirementId;
    
    /** 父需求ID（用于需求拆分） */
    private Long parentRequirementId;
    
    /** 用户画像ID */
    private Long personaId;
    
    /** 画布ID */
    private Long canvasId;
    
    /** 需求标题 */
    private String title;
    
    /** 显性需求 */
    private String explicitRequirement;
    
    /** 隐性需求 */
    private String implicitRequirement;
    
    /** 需求列表 */
    private String requirementList;
    
    /** 需求优先级（Must:必须有 Should:应该有 Could:可以有 Won't:不会有） */
    private String priority;
    
    /** 用户痛点 */
    private String userPainPoints;
    
    /** 需求的背景和情境 */
    private String requirementContext;
    
    /** 需求分析内容 */
    private String requirementAnalysis;
    
    public Long getRequirementId() {
        return requirementId;
    }
    
    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }
    
    public Long getParentRequirementId() {
        return parentRequirementId;
    }
    
    public void setParentRequirementId(Long parentRequirementId) {
        this.parentRequirementId = parentRequirementId;
    }
    
    public Long getPersonaId() {
        return personaId;
    }
    
    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }
    
    public Long getCanvasId() {
        return canvasId;
    }
    
    public void setCanvasId(Long canvasId) {
        this.canvasId = canvasId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getExplicitRequirement() {
        return explicitRequirement;
    }
    
    public void setExplicitRequirement(String explicitRequirement) {
        this.explicitRequirement = explicitRequirement;
    }
    
    public String getImplicitRequirement() {
        return implicitRequirement;
    }
    
    public void setImplicitRequirement(String implicitRequirement) {
        this.implicitRequirement = implicitRequirement;
    }
    
    public String getRequirementList() {
        return requirementList;
    }
    
    public void setRequirementList(String requirementList) {
        this.requirementList = requirementList;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getUserPainPoints() {
        return userPainPoints;
    }
    
    public void setUserPainPoints(String userPainPoints) {
        this.userPainPoints = userPainPoints;
    }
    
    public String getRequirementContext() {
        return requirementContext;
    }
    
    public void setRequirementContext(String requirementContext) {
        this.requirementContext = requirementContext;
    }
    
    public String getRequirementAnalysis() {
        return requirementAnalysis;
    }
    
    public void setRequirementAnalysis(String requirementAnalysis) {
        this.requirementAnalysis = requirementAnalysis;
    }
}

