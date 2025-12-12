package com.gameengine.system.domain.dto;

import lombok.Data;
import java.util.List;

/**
 * DeepSeek 聊天响应 DTO
 * 
 * @author GameEngine
 */
@Data
public class DeepSeekChatResponse {
    
    /**
     * 响应 ID
     */
    private String id;
    
    /**
     * 对象类型
     */
    private String object;
    
    /**
     * 创建时间戳
     */
    private Long created;
    
    /**
     * 模型名称
     */
    private String model;
    
    /**
     * 选择列表
     */
    private List<Choice> choices;
    
    /**
     * 使用情况
     */
    private Usage usage;
    
    /**
     * 选择项
     */
    @Data
    public static class Choice {
        /**
         * 索引
         */
        private Integer index;
        
        /**
         * 消息
         */
        private DeepSeekMessage message;
        
        /**
         * 完成原因
         */
        private String finishReason;
    }
    
    /**
     * 使用情况
     */
    @Data
    public static class Usage {
        /**
         * 提示 token 数
         */
        private Integer promptTokens;
        
        /**
         * 完成 token 数
         */
        private Integer completionTokens;
        
        /**
         * 总 token 数
         */
        private Integer totalTokens;
    }
}

