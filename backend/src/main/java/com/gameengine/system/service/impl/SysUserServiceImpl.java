package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.common.utils.SecurityUtils;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.mapper.SysUserMapper;
import com.gameengine.system.service.ISysUserService;

/**
 * 用户 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    
    @Autowired
    private SysUserMapper userMapper;
    
    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }
    
    /**
     * 根据用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }
    
    /**
     * 根据用户ID查询用户所属角色组
     * 
     * @param userId 用户ID
     * @return 角色组
     */
    @Override
    public List<String> selectUserRoleGroup(Long userId) {
        return userMapper.selectUserRoleGroup(userId);
    }
    
    /**
     * 根据用户ID查询用户权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = userMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (perm != null && !perm.isEmpty()) {
                permsSet.addAll(java.util.Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
    
    /**
     * 登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @return 用户对象信息
     */
    @Override
    public SysUser login(String username, String password) {
        SysUser user = selectUserByUserName(username);
        if (user == null) {
            throw new ServiceException("user.not.exists");
        }
        if (!"0".equals(user.getStatus())) {
            throw new ServiceException("user.disabled");
        }
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            throw new ServiceException("password.error");
        }
        return user;
    }
    
    /**
     * 记录登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     */
    @Override
    public void recordLoginInfo(Long userId, String loginIp) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setLoginIp(loginIp);
        user.setLoginDate(new Date());
        userMapper.updateById(user);
    }
}

