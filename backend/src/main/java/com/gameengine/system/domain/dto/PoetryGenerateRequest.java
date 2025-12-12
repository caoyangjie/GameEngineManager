package com.gameengine.system.domain.dto;

import java.util.List;

/**
 * 诗词生成请求
 */
public class PoetryGenerateRequest {
    
    /** 用户输入的提示词/主题 */
    private String prompt;
    
    /** 目标数量 */
    private Integer count;
    
    /** 难度：入门/进阶/挑战 */
    private String difficulty;
    
    /** 领域或场景 */
    private String domain;
    
    /** 标签 */
    private List<String> tags;
    
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
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
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

