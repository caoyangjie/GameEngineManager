package com.gameengine.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.common.utils.MessageUtils;
import com.gameengine.common.utils.QRCodeUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.dto.DepositAddressResponse;
import com.gameengine.system.service.ISysConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 存款管理
 * 
 * @author GameEngine
 */
@Tag(name = "存款管理", description = "存款地址和二维码管理")
@RestController
@RequestMapping("/deposit")
public class DepositController extends BaseController {
    
    @Autowired
    private ISysConfigService configService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取存款地址和二维码
     * 
     * @param request HTTP请求
     * @return 存款地址和二维码信息
     */
    @Operation(summary = "获取存款地址和二维码", description = "获取BEP20和TRC20地址及其对应的二维码")
    @GetMapping("/getAddresses")
    public AjaxResult getAddresses(HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            // 从配置表获取地址
            String bep20Address = configService.selectConfigValueByKey("deposit.bep20.address");
            String trc20Address = configService.selectConfigValueByKey("deposit.trc20.address");
            
            if (bep20Address == null || bep20Address.trim().isEmpty()) {
                bep20Address = "";
            }
            if (trc20Address == null || trc20Address.trim().isEmpty()) {
                trc20Address = "";
            }
            
            // 生成二维码
            String bep20QrCode = "";
            String trc20QrCode = "";
            
            if (!bep20Address.isEmpty()) {
                bep20QrCode = QRCodeUtils.generateQRCodeBase64(bep20Address);
            }
            if (!trc20Address.isEmpty()) {
                trc20QrCode = QRCodeUtils.generateQRCodeBase64(trc20Address);
            }
            
            DepositAddressResponse response = new DepositAddressResponse();
            response.setBep20Address(bep20Address);
            response.setTrc20Address(trc20Address);
            response.setBep20QRCode(bep20QrCode);
            response.setTrc20QRCode(trc20QrCode);
            
            return success(response);
        } catch (Exception e) {
            logger.error("获取存款地址失败", e);
            return AjaxResult.error(500, "获取存款地址失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取请求token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }
}

