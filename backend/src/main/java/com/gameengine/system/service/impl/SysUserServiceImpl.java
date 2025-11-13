package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    
    private static final String LOCK_STATUS = "1";
    private static final String UNLOCK_STATUS = "0";
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
        if (LOCK_STATUS.equals(user.getStatus())) {
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
    
    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByEmail(String email) {
        return userMapper.selectOne(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, email)
                .eq(SysUser::getDelFlag, "0")
        );
    }
    
    /**
     * 用户注册
     * 
     * @param firstName 名字
     * @param lastName 姓氏
     * @param email 邮箱
     * @param password 密码
     * @param recruiterId 招聘者ID（可选）
     * @return 用户对象信息
     */
    @Override
    public SysUser register(String firstName, String lastName, String email, String password, String recruiterId) {
        // 检查邮箱是否已存在
        SysUser existUser = selectUserByEmail(email);
        if (existUser != null) {
            throw new ServiceException("register.email.exists");
        }
        
        // 检查用户名是否已存在（使用邮箱作为用户名）
        SysUser existUserByName = selectUserByUserName(email);
        if (existUserByName != null) {
            throw new ServiceException("register.username.exists");
        }
        
        // 创建新用户
        SysUser user = new SysUser();
        user.setUserName(email); // 使用邮箱作为用户名
        user.setNickName(firstName + " " + lastName); // 昵称使用名字+姓氏
        user.setEmail(email);
        user.setPassword(SecurityUtils.encryptPassword(password)); // 加密密码
        user.setStatus(UNLOCK_STATUS); // 正常状态
        user.setDelFlag("0"); // 未删除
        user.setSex("0"); // 未知性别
        user.setCreateTime(new Date());
        
        // 如果有招聘者ID，可以存储到备注中（或者创建新字段）
        if (recruiterId != null && !recruiterId.trim().isEmpty()) {
            user.setRemark("RecruiterId: " + recruiterId);
        }
        
        // 插入用户
        userMapper.insert(user);
        
        return user;
    }
    
    /**
     * 发送密码重置邮件
     * 
     * @param email 邮箱
     */
    @Override
    public void sendPasswordResetEmail(String email) {
        SysUser user = selectUserByEmail(email);
        if (user == null) {
            throw new ServiceException("forgotPassword.email.not.exists");
        }
        
        // TODO: 这里应该发送密码重置邮件
        // 可以生成重置令牌，存储到数据库或Redis，然后发送邮件
        // 目前仅做验证邮箱存在性
        // 实际实现中应该：
        // 1. 生成重置令牌
        // 2. 存储令牌（可以存到Redis，设置过期时间）
        // 3. 发送包含重置链接的邮件
    }
}

