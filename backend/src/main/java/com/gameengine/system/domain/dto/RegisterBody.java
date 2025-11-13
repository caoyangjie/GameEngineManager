package com.gameengine.system.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户注册对象
 * 
 * @author GameEngine
 */
@Schema(description = "用户注册请求")
public class RegisterBody {
    
    /**
     * 名字
     */
    @Schema(description = "名字", example = "张", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.firstName.not.blank}")
    private String firstName;
    
    /**
     * 姓氏
     */
    @Schema(description = "姓氏", example = "三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.lastName.not.blank}")
    private String lastName;
    
    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱", example = "zhangsan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.email.not.blank}")
    @Email(message = "{validate.email.invalid}")
    private String email;
    
    /**
     * 招聘者用户ID（可选）
     */
    @Schema(description = "招聘者用户ID", example = "")
    private String recruiterId;
    
    /**
     * 用户密码
     */
    @Schema(description = "用户密码", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.password.not.blank}")
    private String password;
    
    /**
     * 确认密码
     */
    @Schema(description = "确认密码", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.confirmPassword.not.blank}")
    private String confirmPassword;
    
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
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRecruiterId() {
        return recruiterId;
    }
    
    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

