package com.gameengine.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 国际化配置
 * 
 * @author GameEngine
 */
@Configuration
public class LocaleConfig {
    
    /**
     * 配置自定义国际化解析器
     * 支持从请求头获取语言设置，优先级：
     * 1. 自定义请求头 "lang" 或 "locale"
     * 2. Accept-Language 请求头
     * 3. 默认语言（简体中文）
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }
}

