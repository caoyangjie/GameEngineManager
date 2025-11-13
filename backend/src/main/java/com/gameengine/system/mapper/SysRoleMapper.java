package com.gameengine.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.SysRole;

import java.util.List;

/**
 * 角色表 数据层
 * 
 * @author GameEngine
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    
    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);
    
    /**
     * 根据角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SysRole selectRoleById(Long roleId);
}

