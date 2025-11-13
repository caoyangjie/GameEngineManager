package com.gameengine.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 验证码结果对象
 * 
 * @author GameEngine
 */
@Schema(description = "验证码结果")
public class CaptchaResult {
    
    /**
     * 验证码唯一标识
     */
    @Schema(description = "验证码唯一标识", example = "uuid-123456")
    private String uuid;
    
    /**
     * 验证码图片（Base64编码）
     */
    @Schema(description = "验证码图片（Base64编码）", example = "data:image/png;base64,...")
    private String img;
    
    /**
     * 验证码文本（用于测试，生产环境不返回）
     */
    @Schema(description = "验证码文本（仅开发环境）", example = "1234")
    private String code;
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getImg() {
        return img;
    }
    
    public void setImg(String img) {
        this.img = img;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}

