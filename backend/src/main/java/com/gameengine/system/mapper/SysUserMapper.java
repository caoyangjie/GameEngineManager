package com.gameengine.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.SysUser;

/**
 * 用户表 数据层
 * 
 * @author GameEngine
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);
    
    /**
     * 根据用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);
    
    /**
     * 根据用户ID查询用户所属角色组
     * 
     * @param userId 用户ID
     * @return 角色组
     */
    List<String> selectUserRoleGroup(Long userId);
    
    /**
     * 根据用户ID查询用户权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);
}

