package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 识字测试记录对象 character_test_record
 * 
 * @author GameEngine
 */
@TableName("character_test_record")
public class CharacterTestRecord {
    
    private static final long serialVersionUID = 1L;
    
    /** 测试记录ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 用户ID，用于数据隔离 */
    private Long userId;
    
    /** 学生姓名 */
    private String studentName;
    
    /** 测试日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testDate;
    
    /** 教育阶段: primary(小学), middle(初中), high(高中) */
    private String educationLevel;
    
    /** 测试字数 */
    private Integer testCount;
    
    /** 是否显示拼音 */
    private Boolean showPinyin;
    
    /** 测试字符数据（JSON格式） */
    private String characters;
    
    /** 正确数量 */
    private Integer correctCount;
    
    /** 错误数量 */
    private Integer incorrectCount;
    
    /** 掌握数量 */
    private Integer masteredCount;
    
    /** 掌握率（百分比） */
    private Integer masteryRate;
    
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
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public Date getTestDate() {
        return testDate;
    }
    
    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }
    
    public String getEducationLevel() {
        return educationLevel;
    }
    
    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }
    
    public Integer getTestCount() {
        return testCount;
    }
    
    public void setTestCount(Integer testCount) {
        this.testCount = testCount;
    }
    
    public Boolean getShowPinyin() {
        return showPinyin;
    }
    
    public void setShowPinyin(Boolean showPinyin) {
        this.showPinyin = showPinyin;
    }
    
    public String getCharacters() {
        return characters;
    }
    
    public void setCharacters(String characters) {
        this.characters = characters;
    }
    
    public Integer getCorrectCount() {
        return correctCount;
    }
    
    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }
    
    public Integer getIncorrectCount() {
        return incorrectCount;
    }
    
    public void setIncorrectCount(Integer incorrectCount) {
        this.incorrectCount = incorrectCount;
    }
    
    public Integer getMasteredCount() {
        return masteredCount;
    }
    
    public void setMasteredCount(Integer masteredCount) {
        this.masteredCount = masteredCount;
    }
    
    public Integer getMasteryRate() {
        return masteryRate;
    }
    
    public void setMasteryRate(Integer masteryRate) {
        this.masteryRate = masteryRate;
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

