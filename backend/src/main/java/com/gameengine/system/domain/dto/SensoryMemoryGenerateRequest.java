package com.gameengine.system.domain.dto;

import java.util.List;

/**
 * 感官记忆内容生成请求
 */
public class SensoryMemoryGenerateRequest {
    
    /** 用户输入的提示词/主题 */
    private String prompt;
    
    /** 关键词/关键句子（可选） */
    private String keywords;
    
    /** 目标数量（5-10个） */
    private Integer count;
    
    /** 难度：入门/进阶/挑战 */
    private String difficulty;
    
    /** 内容类型：word(词语) / sentence(句子) */
    private String contentType;
    
    /** 标签 */
    private List<String> tags;
    
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    public String getKeywords() {
        return keywords;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

