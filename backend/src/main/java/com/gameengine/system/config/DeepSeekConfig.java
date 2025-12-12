package com.gameengine.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * DeepSeek AI 配置类
 * 
 * @author GameEngine
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekConfig {
    
    /**
     * API Key
     */
    private String apiKey;
    
    /**
     * API 基础 URL
     */
    private String baseUrl = "https://api.deepseek.com";
    
    /**
     * 默认模型
     */
    private String defaultModel = "deepseek-chat";
    
    /**
     * 请求超时时间（毫秒）
     */
    private Integer timeout = 60000;
    
    /**
     * 最大重试次数
     */
    private Integer maxRetries = 3;
}

