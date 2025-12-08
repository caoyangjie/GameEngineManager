package com.gameengine.system.domain.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 识字测试记录DTO
 * 
 * @author GameEngine
 */
public class CharacterTestRecordDTO {
    
    /** 测试记录ID */
    private Long id;
    
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
    
    /** 测试字符列表 */
    private List<CharacterTestItemDTO> characters;
    
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
    
    /**
     * 测试字符项DTO
     */
    public static class CharacterTestItemDTO {
        private String character;
        private String pinyin;
        private String grade;
        private String status; // null: 未标记, 'correct': 正确, 'incorrect': 错误
        
        public String getCharacter() {
            return character;
        }
        
        public void setCharacter(String character) {
            this.character = character;
        }
        
        public String getPinyin() {
            return pinyin;
        }
        
        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
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
    
    public List<CharacterTestItemDTO> getCharacters() {
        return characters;
    }
    
    public void setCharacters(List<CharacterTestItemDTO> characters) {
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

