package com.gameengine.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gameengine.common.core.domain.BaseEntity;

/**
 * 用户场景对象 scenario
 * 
 * @author GameEngine
 */
@TableName("scenario")
public class Scenario extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 场景ID */
    @TableId(type = IdType.AUTO)
    private Long scenarioId;
    
    /** 用户画像ID */
    private Long personaId;
    
    /** 画布ID */
    private Long canvasId;
    
    /** 场景标题 */
    private String title;
    
    /** 用户角色 */
    private String userRole;
    
    /** 环境 */
    private String environment;
    
    /** 目标 */
    private String goal;
    
    /** 动机 */
    private String motivation;
    
    /** 情境状况 */
    private String situation;
    
    /** 行为流程 */
    private String behaviorFlow;
    
    /** 障碍与痛点 */
    private String obstacles;
    
    /** 结果与期望 */
    private String resultExpectation;
    
    public Long getScenarioId() {
        return scenarioId;
    }
    
    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
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
    
    public String getUserRole() {
        return userRole;
    }
    
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public String getGoal() {
        return goal;
    }
    
    public void setGoal(String goal) {
        this.goal = goal;
    }
    
    public String getMotivation() {
        return motivation;
    }
    
    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }
    
    public String getSituation() {
        return situation;
    }
    
    public void setSituation(String situation) {
        this.situation = situation;
    }
    
    public String getBehaviorFlow() {
        return behaviorFlow;
    }
    
    public void setBehaviorFlow(String behaviorFlow) {
        this.behaviorFlow = behaviorFlow;
    }
    
    public String getObstacles() {
        return obstacles;
    }
    
    public void setObstacles(String obstacles) {
        this.obstacles = obstacles;
    }
    
    public String getResultExpectation() {
        return resultExpectation;
    }
    
    public void setResultExpectation(String resultExpectation) {
        this.resultExpectation = resultExpectation;
    }
}

