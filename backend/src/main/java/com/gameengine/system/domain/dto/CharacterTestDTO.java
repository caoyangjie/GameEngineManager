package com.gameengine.system.domain.dto;

/**
 * 识字测试DTO
 * 
 * @author GameEngine
 */
public class CharacterTestDTO {
    
    /** 汉字 */
    private String character;
    
    /** 拼音 */
    private String pinyin;
    
    /** 教育阶段: primary(小学), middle(初中), high(高中) */
    private String educationLevel;

    /** 年级标识，如 primary-1 表示小学一年级 */
    private String grade;
    
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
    
    public String getEducationLevel() {
        return educationLevel;
    }
    
    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

