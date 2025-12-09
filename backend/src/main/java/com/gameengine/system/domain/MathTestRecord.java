package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数学测试记录对象 math_test_record
 * 
 * @author GameEngine
 */
@TableName("math_test_record")
public class MathTestRecord {
    
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
    
    /** 测试题目数量 */
    private Integer testCount;
    
    /** 计算类型（JSON格式，包含加减乘除的选择） */
    private String operationTypes;
    
    /** 数字范围最小值 */
    private Integer minNumber;
    
    /** 数字范围最大值 */
    private Integer maxNumber;
    
    /** 测试题目数据（JSON格式） */
    private String questions;
    
    /** 正确数量 */
    private Integer correctCount;
    
    /** 错误数量 */
    private Integer incorrectCount;
    
    /** 正确率（百分比） */
    private Integer accuracyRate;
    
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
    
    public Integer getTestCount() {
        return testCount;
    }
    
    public void setTestCount(Integer testCount) {
        this.testCount = testCount;
    }
    
    public String getOperationTypes() {
        return operationTypes;
    }
    
    public void setOperationTypes(String operationTypes) {
        this.operationTypes = operationTypes;
    }
    
    public Integer getMinNumber() {
        return minNumber;
    }
    
    public void setMinNumber(Integer minNumber) {
        this.minNumber = minNumber;
    }
    
    public Integer getMaxNumber() {
        return maxNumber;
    }
    
    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }
    
    public String getQuestions() {
        return questions;
    }
    
    public void setQuestions(String questions) {
        this.questions = questions;
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
    
    public Integer getAccuracyRate() {
        return accuracyRate;
    }
    
    public void setAccuracyRate(Integer accuracyRate) {
        this.accuracyRate = accuracyRate;
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

