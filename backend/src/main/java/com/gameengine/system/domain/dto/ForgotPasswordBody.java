package com.gameengine.system.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 忘记密码对象
 * 
 * @author GameEngine
 */
@Schema(description = "忘记密码请求")
public class ForgotPasswordBody {
    
    /**
     * 注册电子邮箱
     */
    @Schema(description = "注册电子邮箱", example = "zhangsan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.email.not.blank}")
    @Email(message = "{validate.email.invalid}")
    private String email;
    
    /**
     * 验证码
     */
    @Schema(description = "验证码", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.code.not.blank}")
    private String code;
    
    /**
     * 唯一标识
     */
    @Schema(description = "唯一标识", example = "uuid-123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.uuid.not.blank}")
    private String uuid;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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

