package com.gameengine.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.SysMenu;

import java.util.List;

/**
 * 菜单表 数据层
 * 
 * @author GameEngine
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    
    /**
     * 根据用户ID查询菜单
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);
    
    /**
     * 根据角色ID查询菜单树信息
     * 
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Integer> selectMenuListByRoleId(Long roleId);
    
    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);
}

