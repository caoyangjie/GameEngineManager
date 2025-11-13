package com.gameengine.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录结果对象
 * 
 * @author GameEngine
 */
@Schema(description = "登录结果")
public class LoginResult {
    
    /**
     * 访问令牌
     */
    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}

