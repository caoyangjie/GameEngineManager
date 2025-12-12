package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 成语资料对象 attention_idiom_info
 */
@TableName("attention_idiom_info")
public class AttentionIdiomInfo {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 成语文本 */
    private String idiom;
    
    /** 拼音 */
    private String pinyin;
    
    /** 字面含义 */
    private String literalMeaning;
    
    /** 释义/引申义 */
    private String meaning;
    
    /** 寓意/启示 */
    private String moral;
    
    /** 产生朝代 */
    private String originDynasty;
    
    /** 出处/典籍 */
    private String originSource;
    
    /** 产生背景 */
    private String background;
    
    /** 背景故事 */
    private String story;
    
    /** 主人公/主角 */
    private String protagonist;
    
    /** 关联人物 */
    private String relatedPersons;
    
    /** 使用场景/例句（列名使用 usage_text 以规避保留字） */
    @TableField("usage_text")
    private String usageText;
    
    /** 介绍成语的视频地址 */
    private String videoUrl;
    
    /** 记忆线索/桩子 */
    private String memoryCues;
    
    /** 标签 */
    private String tags;
    
    /** 创建人 */
    private Long createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIdiom() {
        return idiom;
    }
    
    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }
    
    public String getPinyin() {
        return pinyin;
    }
    
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    
    public String getLiteralMeaning() {
        return literalMeaning;
    }
    
    public void setLiteralMeaning(String literalMeaning) {
        this.literalMeaning = literalMeaning;
    }
    
    public String getMeaning() {
        return meaning;
    }
    
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
    
    public String getMoral() {
        return moral;
    }
    
    public void setMoral(String moral) {
        this.moral = moral;
    }
    
    public String getOriginDynasty() {
        return originDynasty;
    }
    
    public void setOriginDynasty(String originDynasty) {
        this.originDynasty = originDynasty;
    }
    
    public String getOriginSource() {
        return originSource;
    }
    
    public void setOriginSource(String originSource) {
        this.originSource = originSource;
    }
    
    public String getBackground() {
        return background;
    }
    
    public void setBackground(String background) {
        this.background = background;
    }
    
    public String getStory() {
        return story;
    }
    
    public void setStory(String story) {
        this.story = story;
    }
    
    public String getProtagonist() {
        return protagonist;
    }
    
    public void setProtagonist(String protagonist) {
        this.protagonist = protagonist;
    }
    
    public String getRelatedPersons() {
        return relatedPersons;
    }
    
    public void setRelatedPersons(String relatedPersons) {
        this.relatedPersons = relatedPersons;
    }
    
    public String getUsageText() {
        return usageText;
    }
    
    public void setUsageText(String usageText) {
        this.usageText = usageText;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public String getMemoryCues() {
        return memoryCues;
    }
    
    public void setMemoryCues(String memoryCues) {
        this.memoryCues = memoryCues;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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


