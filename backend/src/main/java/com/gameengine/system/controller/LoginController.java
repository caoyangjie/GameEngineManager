package com.gameengine.system.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.common.utils.CaptchaStore;
import com.gameengine.common.utils.CaptchaUtils;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.common.utils.MessageUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.domain.dto.CaptchaResult;
import com.gameengine.system.domain.dto.ForgotPasswordBody;
import com.gameengine.system.domain.dto.LoginBody;
import com.gameengine.system.domain.dto.LoginResult;
import com.gameengine.system.domain.dto.RegisterBody;
import com.gameengine.system.domain.dto.UserInfo;
import com.gameengine.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 登录验证
 * 
 * @author GameEngine
 */
@Tag(name = "登录管理", description = "用户登录、登出、获取用户信息")
@RestController
public class LoginController extends BaseController {
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private CaptchaUtils captchaUtils;
    
    @Value("${gameengine.demoEnabled:false}")
    private boolean demoEnabled;
    
    /**
     * 获取验证码
     * 
     * @return 验证码信息
     */
    @Operation(summary = "获取验证码", description = "获取登录验证码")
    @GetMapping("/captchaImage")
    public AjaxResult getCaptchaImage() {
        // 生成UUID
        String uuid = UUID.randomUUID().toString();
        
        // 生成验证码
        String[] captcha = captchaUtils.generateCaptcha();
        String code = captcha[0];
        String img = captcha[1];
        
        // 存储验证码（5分钟过期）
        CaptchaStore.store(uuid, code);
        
        CaptchaResult result = new CaptchaResult();
        result.setUuid(uuid);
        result.setImg(img);
        // 开发环境返回验证码文本，方便测试
        if (demoEnabled) {
            result.setCode(code);
        }
        
        return AjaxResult.success(result);
    }
    
    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @Operation(summary = "用户登录", description = "根据用户名和密码登录系统")
    @PostMapping("/login")
    public AjaxResult login(@Valid @RequestBody LoginBody loginBody, HttpServletRequest request) {
        // 验证验证码
        if (loginBody.getUuid() != null && !loginBody.getUuid().isEmpty()) {
            validateCaptcha(loginBody.getUuid(), loginBody.getCode());
        }
        
        // 用户验证
        SysUser user = userService.login(loginBody.getUsername(), loginBody.getPassword());
        
        // 生成token
        String token = jwtUtils.createToken(user.getUserId(), user.getUserName());
        
        // 记录登录信息
        String loginIp = getIpAddr(request);
        userService.recordLoginInfo(user.getUserId(), loginIp);
        
        // 删除验证码
        if (loginBody.getUuid() != null && !loginBody.getUuid().isEmpty()) {
            CaptchaStore.remove(loginBody.getUuid());
        }
        
        LoginResult loginResult = new LoginResult();
        loginResult.setToken(token);
        
        return AjaxResult.success(MessageUtils.message("login.success"), loginResult);
    }
    
    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据token获取当前登录用户信息")
    @GetMapping("/getInfo")
    public AjaxResult getInfo(HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        SysUser user = userService.selectUserById(userId);
        if (user == null) {
            return AjaxResult.error(401, MessageUtils.message("user.not.exists"));
        }
        
        // 角色集合
        List<String> roles = userService.selectUserRoleGroup(userId);
        
        // 权限集合
        Set<String> permissions = userService.selectMenuPermsByUserId(userId);
        
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setRoles(roles);
        userInfo.setPermissions(permissions);
        
        return success(userInfo);
    }
    
    /**
     * 退出登录
     * 
     * @return 结果
     */
    @Operation(summary = "退出登录", description = "用户退出登录")
    @PostMapping("/logout")
    public AjaxResult logout() {
        return success(MessageUtils.message("logout.success"));
    }
    
    /**
     * 用户注册
     * 
     * @param registerBody 注册信息
     * @return 结果
     */
    @Operation(summary = "用户注册", description = "新用户注册")
    @PostMapping("/register")
    public AjaxResult register(@Valid @RequestBody RegisterBody registerBody) {
        // 验证验证码
        if (registerBody.getUuid() != null && !registerBody.getUuid().isEmpty()) {
            validateCaptcha(registerBody.getUuid(), registerBody.getCode());
        }
        
        // 验证密码是否匹配
        if (!registerBody.getPassword().equals(registerBody.getConfirmPassword())) {
            throw new ServiceException(MessageUtils.message("register.password.mismatch"), 400);
        }
        
        // 注册用户
        userService.register(
            registerBody.getFirstName(),
            registerBody.getLastName(),
            registerBody.getEmail(),
            registerBody.getPassword(),
            registerBody.getRecruiterId()
        );
        
        // 删除验证码
        if (registerBody.getUuid() != null && !registerBody.getUuid().isEmpty()) {
            CaptchaStore.remove(registerBody.getUuid());
        }
        
        return AjaxResult.success(MessageUtils.message("register.success"));
    }
    
    /**
     * 忘记密码
     * 
     * @param forgotPasswordBody 忘记密码信息
     * @return 结果
     */
    @Operation(summary = "忘记密码", description = "发送密码重置邮件")
    @PostMapping("/forgotPassword")
    public AjaxResult forgotPassword(@Valid @RequestBody ForgotPasswordBody forgotPasswordBody) {
        // 验证验证码
        if (forgotPasswordBody.getUuid() != null && !forgotPasswordBody.getUuid().isEmpty()) {
            validateCaptcha(forgotPasswordBody.getUuid(), forgotPasswordBody.getCode());
        }
        
        // 发送密码重置邮件
        userService.sendPasswordResetEmail(forgotPasswordBody.getEmail());
        
        // 删除验证码
        if (forgotPasswordBody.getUuid() != null && !forgotPasswordBody.getUuid().isEmpty()) {
            CaptchaStore.remove(forgotPasswordBody.getUuid());
        }
        
        return AjaxResult.success(MessageUtils.message("forgotPassword.resetLinkSent"));
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
    
    /**
     * 验证验证码
     * 
     * @param uuid 验证码UUID
     * @param code 验证码
     */
    private void validateCaptcha(String uuid, String code) {
        CaptchaStore.validate(uuid, code, MessageUtils.message("captcha.expired"), MessageUtils.message("captcha.error"));
    }
    
    /**
     * 获取客户端IP
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}

