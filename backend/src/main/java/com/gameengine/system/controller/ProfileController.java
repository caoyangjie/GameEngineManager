package com.gameengine.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.common.utils.MessageUtils;
import com.gameengine.common.utils.QRCodeUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.SysUserExt;
import com.gameengine.system.domain.dto.UpdatePasswordBody;
import com.gameengine.system.domain.dto.UpdateUserExtBody;
import com.gameengine.system.service.ISysUserExtService;
import com.gameengine.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 个人资料管理
 * 
 * @author GameEngine
 */
@Tag(name = "个人资料管理", description = "用户个人资料、扩展信息、密码管理")
@RestController
@RequestMapping("/profile")
public class ProfileController extends BaseController {
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysUserExtService userExtService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取用户扩展信息
     * 
     * @param request HTTP请求
     * @return 用户扩展信息
     */
    @Operation(summary = "获取用户扩展信息", description = "获取当前登录用户的扩展信息")
    @GetMapping("/getExtInfo")
    public AjaxResult getExtInfo(HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        SysUserExt userExt = userExtService.selectByUserId(userId);
        if (userExt == null) {
            // 如果没有扩展信息，返回空对象
            userExt = new SysUserExt();
            userExt.setUserId(userId);
            userExt.setRecruitmentLink("");
            userExt.setCurrentLevel("");
            userExt.setPlayerId("");
            userExt.setBep20Address("");
        }
        
        return success(userExt);
    }
    
    /**
     * 更新用户扩展信息
     * 
     * @param updateBody 更新信息
     * @param request HTTP请求
     * @return 结果
     */
    @Operation(summary = "更新用户扩展信息", description = "更新当前登录用户的扩展信息")
    @PostMapping("/updateExtInfo")
    public AjaxResult updateExtInfo(@Valid @RequestBody UpdateUserExtBody updateBody, HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        // 更新扩展信息
        userExtService.updateUserExt(
            userId,
            updateBody.getRecruitmentLink(),
            updateBody.getCurrentLevel(),
            updateBody.getPlayerId(),
            updateBody.getBep20Address()
        );
        
        return AjaxResult.success(MessageUtils.message("profile.updateExtInfo.success"));
    }
    
    /**
     * 更新用户密码
     * 
     * @param updateBody 更新密码信息
     * @param request HTTP请求
     * @return 结果
     */
    @Operation(summary = "更新用户密码", description = "更新当前登录用户的密码")
    @PostMapping("/updatePassword")
    public AjaxResult updatePassword(@Valid @RequestBody UpdatePasswordBody updateBody, HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        // 验证新密码和确认密码是否一致
        if (!updateBody.getNewPassword().equals(updateBody.getConfirmPassword())) {
            throw new ServiceException(MessageUtils.message("register.password.mismatch"), 400);
        }
        
        // 更新密码
        userService.updatePassword(userId, updateBody.getOldPassword(), updateBody.getNewPassword());
        
        return AjaxResult.success(MessageUtils.message("profile.updatePassword.success"));
    }
    
    /**
     * 生成二维码
     * 
     * @param content 二维码内容
     * @param request HTTP请求
     * @return 二维码Base64图片
     */
    @Operation(summary = "生成二维码", description = "根据给定内容生成二维码图片")
    @GetMapping("/generateQRCode")
    public AjaxResult generateQRCode(String content, HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            if (content == null || content.trim().isEmpty()) {
                content = "";
            }
            String qrCodeBase64 = QRCodeUtils.generateQRCodeBase64(content);
            return AjaxResult.success("success", qrCodeBase64);
        } catch (Exception e) {
            return AjaxResult.error(500, "生成二维码失败: " + e.getMessage());
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

