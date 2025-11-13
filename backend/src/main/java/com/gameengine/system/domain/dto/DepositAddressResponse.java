package com.gameengine.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 存款地址响应对象
 * 
 * @author GameEngine
 */
@Schema(description = "存款地址信息")
public class DepositAddressResponse {
    
    /**
     * BEP20地址
     */
    @Schema(description = "BEP20地址", example = "0xCc3df0Ccdec9D6ADCD3AfD999D1282Bd1939d8cd")
    private String bep20Address;
    
    /**
     * TRC20地址
     */
    @Schema(description = "TRC20地址", example = "TQfvBWeQwkvHXaaZZbdfZ6YGxENgyqqwMn")
    private String trc20Address;
    
    /**
     * BEP20二维码Base64
     */
    @Schema(description = "BEP20二维码Base64图片", example = "data:image/png;base64,iVBORw0KGgo...")
    private String bep20QRCode;
    
    /**
     * TRC20二维码Base64
     */
    @Schema(description = "TRC20二维码Base64图片", example = "data:image/png;base64,iVBORw0KGgo...")
    private String trc20QRCode;
    
    public String getBep20Address() {
        return bep20Address;
    }
    
    public void setBep20Address(String bep20Address) {
        this.bep20Address = bep20Address;
    }
    
    public String getTrc20Address() {
        return trc20Address;
    }
    
    public void setTrc20Address(String trc20Address) {
        this.trc20Address = trc20Address;
    }
    
    public String getBep20QRCode() {
        return bep20QRCode;
    }
    
    public void setBep20QRCode(String bep20QRCode) {
        this.bep20QRCode = bep20QRCode;
    }
    
    public String getTrc20QRCode() {
        return trc20QRCode;
    }
    
    public void setTrc20QRCode(String trc20QRCode) {
        this.trc20QRCode = trc20QRCode;
    }
}

