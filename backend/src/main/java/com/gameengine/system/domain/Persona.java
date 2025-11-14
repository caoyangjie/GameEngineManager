package com.gameengine.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gameengine.common.core.domain.BaseEntity;

/**
 * 用户画像对象 persona
 * 
 * @author GameEngine
 */
@TableName("persona")
public class Persona extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 用户画像ID */
    @TableId(type = IdType.AUTO)
    private Long personaId;
    
    /** 画布ID */
    private Long canvasId;
    
    /** 姓名 */
    private String name;
    
    /** 年龄 */
    private Integer age;
    
    /** 性别（male:男 female:女 other:其他） */
    private String gender;
    
    /** 身份 */
    private String identity;
    
    /** 爱好 */
    private String hobbies;
    
    /** 使用产品的场景 */
    private String usageScenario;
    
    /** 需求分析内容 */
    private String requirementAnalysis;
    
    /** 头像地址 */
    private String avatar;
    
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getIdentity() {
        return identity;
    }
    
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    
    public String getHobbies() {
        return hobbies;
    }
    
    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
    
    public String getUsageScenario() {
        return usageScenario;
    }
    
    public void setUsageScenario(String usageScenario) {
        this.usageScenario = usageScenario;
    }
    
    public String getRequirementAnalysis() {
        return requirementAnalysis;
    }
    
    public void setRequirementAnalysis(String requirementAnalysis) {
        this.requirementAnalysis = requirementAnalysis;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

