package com.gameengine.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger 配置类
 * 使用 SpringDoc OpenAPI (兼容 Spring Boot 2.7.x)
 * 支持 Bootstrap UI 版本（Knife4j）
 * 
 * @author GameEngine
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置 OpenAPI 信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("游戏引擎管理系统接口文档")
                        .description("基于Spring Boot的游戏引擎管理系统API接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("GameEngine")
                                .email("")
                                .url("")))
                .components(new Components()
                        .addSecuritySchemes("Authorization", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                                        .description("JWT Token，格式：Bearer {token}")));
    }
}
