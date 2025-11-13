package com.gameengine.system.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.common.utils.MessageUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.domain.dto.LoginBody;
import com.gameengine.system.domain.dto.LoginResult;
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
    
    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @Operation(summary = "用户登录", description = "根据用户名和密码登录系统")
    @PostMapping("/login")
    public AjaxResult login(@Valid @RequestBody LoginBody loginBody, HttpServletRequest request) {
        // 用户验证
        SysUser user = userService.login(loginBody.getUsername(), loginBody.getPassword());
        
        // 生成token
        String token = jwtUtils.createToken(user.getUserId(), user.getUserName());
        
        // 记录登录信息
        String loginIp = getIpAddr(request);
        userService.recordLoginInfo(user.getUserId(), loginIp);
        
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

