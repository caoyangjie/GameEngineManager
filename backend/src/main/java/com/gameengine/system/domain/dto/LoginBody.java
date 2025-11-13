package com.gameengine.system.domain.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户登录对象
 * 
 * @author GameEngine
 */
@Schema(description = "用户登录请求")
public class LoginBody {
    
    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.username.not.blank}")
    private String username;
    
    /**
     * 用户密码
     */
    @Schema(description = "用户密码", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.password.not.blank}")
    private String password;
    
    /**
     * 验证码
     */
    @Schema(description = "验证码", example = "")
    private String code;
    
    /**
     * 唯一标识
     */
    @Schema(description = "唯一标识", example = "")
    private String uuid;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

