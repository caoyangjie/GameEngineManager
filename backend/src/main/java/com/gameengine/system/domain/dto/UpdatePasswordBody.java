package com.gameengine.system.domain.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新密码对象
 * 
 * @author GameEngine
 */
@Schema(description = "更新密码请求")
public class UpdatePasswordBody {
    
    /**
     * 当前密码
     */
    @Schema(description = "当前密码", example = "oldPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.password.not.blank}")
    private String oldPassword;
    
    /**
     * 新密码
     */
    @Schema(description = "新密码", example = "newPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.password.not.blank}")
    private String newPassword;
    
    /**
     * 确认密码
     */
    @Schema(description = "确认密码", example = "newPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{validate.confirmPassword.not.blank}")
    private String confirmPassword;
    
    public String getOldPassword() {
        return oldPassword;
    }
    
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

