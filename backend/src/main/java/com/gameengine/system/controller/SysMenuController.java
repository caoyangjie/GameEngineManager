package com.gameengine.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.SysMenu;
import com.gameengine.system.service.ISysMenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 菜单信息
 * 
 * @author GameEngine
 */
@Tag(name = "菜单管理", description = "菜单和路由管理")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    
    @Autowired
    private ISysMenuService menuService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @Operation(summary = "获取路由信息", description = "获取当前用户的路由菜单")
    @GetMapping("/getRouters")
    public AjaxResult getRouters(HttpServletRequest request) {
        String token = getToken(request);
        Long userId = jwtUtils.getUserIdFromToken(token);
        
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        List<SysMenu> menuList = menuService.buildMenus(menus);
        return success(menuList);
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

