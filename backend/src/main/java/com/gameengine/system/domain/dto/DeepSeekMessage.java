package com.gameengine.system.domain.dto;

import lombok.Data;

/**
 * DeepSeek 消息对象
 * 
 * @author GameEngine
 */
@Data
public class DeepSeekMessage {
    
    /**
     * 角色：system, user, assistant
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String content;
    
    public DeepSeekMessage() {
    }
    
    public DeepSeekMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}

