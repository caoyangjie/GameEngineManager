package com.gameengine.system.domain.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数学测试记录DTO
 * 
 * @author GameEngine
 */
public class MathTestRecordDTO {
    
    /** 测试记录ID */
    private Long id;
    
    /** 学生姓名 */
    private String studentName;
    
    /** 测试日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testDate;
    
    /** 测试题目数量 */
    private Integer testCount;
    
    /** 计算类型列表（加减乘除） */
    private List<String> operationTypes;
    
    /** 数字范围最小值 */
    private Integer minNumber;
    
    /** 数字范围最大值 */
    private Integer maxNumber;
    
    /** 测试题目列表 */
    private List<MathTestQuestionDTO> questions;
    
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
    
    /**
     * 数学测试题目DTO
     */
    public static class MathTestQuestionDTO {
        /** 第一个数字 */
        private Integer num1;
        
        /** 运算符：+、-、*、/ */
        private String operator;
        
        /** 第二个数字 */
        private Integer num2;
        
        /** 正确答案 */
        private Integer correctAnswer;
        
        /** 学生答案 */
        private Integer studentAnswer;
        
        /** 是否正确 */
        private Boolean isCorrect;
        
        public Integer getNum1() {
            return num1;
        }
        
        public void setNum1(Integer num1) {
            this.num1 = num1;
        }
        
        public String getOperator() {
            return operator;
        }
        
        public void setOperator(String operator) {
            this.operator = operator;
        }
        
        public Integer getNum2() {
            return num2;
        }
        
        public void setNum2(Integer num2) {
            this.num2 = num2;
        }
        
        public Integer getCorrectAnswer() {
            return correctAnswer;
        }
        
        public void setCorrectAnswer(Integer correctAnswer) {
            this.correctAnswer = correctAnswer;
        }
        
        public Integer getStudentAnswer() {
            return studentAnswer;
        }
        
        public void setStudentAnswer(Integer studentAnswer) {
            this.studentAnswer = studentAnswer;
        }
        
        public Boolean getIsCorrect() {
            return isCorrect;
        }
        
        public void setIsCorrect(Boolean isCorrect) {
            this.isCorrect = isCorrect;
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public List<String> getOperationTypes() {
        return operationTypes;
    }
    
    public void setOperationTypes(List<String> operationTypes) {
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
    
    public List<MathTestQuestionDTO> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<MathTestQuestionDTO> questions) {
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

