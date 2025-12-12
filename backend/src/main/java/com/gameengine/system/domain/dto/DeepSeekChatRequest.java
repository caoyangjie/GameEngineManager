package com.gameengine.system.domain.dto;

import lombok.Data;
import java.util.List;

/**
 * DeepSeek 聊天请求 DTO
 * 
 * @author GameEngine
 */
@Data
public class DeepSeekChatRequest {
    
    /**
     * 模型名称，默认为 deepseek-chat
     */
    private String model = "deepseek-chat";
    
    /**
     * 消息列表
     */
    private List<DeepSeekMessage> messages;
    
    /**
     * 温度参数，控制输出的随机性，范围 0-2，默认 1.0
     */
    private Double temperature = 1.0;
    
    /**
     * Top-p 采样参数，范围 0-1，默认 1.0
     */
    private Double topP = 1.0;
    
    /**
     * 最大生成 token 数，默认 4096
     */
    private Integer maxTokens = 4096;
    
    /**
     * 是否流式输出，默认 false
     */
    private Boolean stream = false;
    
    /**
     * 频率惩罚，范围 -2.0 到 2.0，默认 0
     */
    private Double frequencyPenalty = 0.0;
    
    /**
     * 存在惩罚，范围 -2.0 到 2.0，默认 0
     */
    private Double presencePenalty = 0.0;
}

