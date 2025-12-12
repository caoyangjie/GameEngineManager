package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 诗词资料对象 attention_poetry_info
 */
@TableName("attention_poetry_info")
public class PoetryInfo {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 诗词标题 */
    private String title;
    
    /** 作者 */
    private String author;
    
    /** 朝代 */
    private String dynasty;
    
    /** 诗词内容（全文） */
    private String content;
    
    /** 拼音标注 */
    private String pinyin;
    
    /** 诗词类型（如：五言绝句、七言律诗等） */
    private String poetryType;
    
    /** 释义/译文 */
    private String meaning;
    
    /** 创作背景 */
    private String background;
    
    /** 诗词赏析 */
    private String appreciation;
    
    /** 主题/情感 */
    private String theme;
    
    /** 关键词/意象 */
    private String keywords;
    
    /** 记忆线索/桩子 */
    private String memoryCues;
    
    /** 使用场景/例句（列名使用 usage_text 以规避保留字） */
    @TableField("usage_text")
    private String usageText;
    
    /** 介绍诗词的视频地址 */
    private String videoUrl;
    
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getDynasty() {
        return dynasty;
    }
    
    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getPinyin() {
        return pinyin;
    }
    
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    
    public String getPoetryType() {
        return poetryType;
    }
    
    public void setPoetryType(String poetryType) {
        this.poetryType = poetryType;
    }
    
    public String getMeaning() {
        return meaning;
    }
    
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
    
    public String getBackground() {
        return background;
    }
    
    public void setBackground(String background) {
        this.background = background;
    }
    
    public String getAppreciation() {
        return appreciation;
    }
    
    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }
    
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public String getKeywords() {
        return keywords;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public String getMemoryCues() {
        return memoryCues;
    }
    
    public void setMemoryCues(String memoryCues) {
        this.memoryCues = memoryCues;
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

