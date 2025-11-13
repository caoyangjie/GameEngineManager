package com.gameengine.system.service;

import com.gameengine.system.domain.SysMenu;

import java.util.List;

/**
 * 菜单 业务层
 * 
 * @author GameEngine
 */
public interface ISysMenuService {
    
    /**
     * 根据用户ID查询菜单
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);
    
    /**
     * 构建前端路由所需要的菜单
     * 
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<SysMenu> buildMenus(List<SysMenu> menus);
}

