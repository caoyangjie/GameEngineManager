package com.gameengine.system.service;

import java.util.List;
import java.util.Set;

import com.gameengine.system.domain.SysUser;

/**
 * 用户 业务层
 * 
 * @author GameEngine
 */
public interface ISysUserService {
    
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
    Set<String> selectMenuPermsByUserId(Long userId);
    
    /**
     * 登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @return 用户对象信息
     */
    SysUser login(String username, String password);
    
    /**
     * 记录登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     */
    void recordLoginInfo(Long userId, String loginIp);
    
    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    SysUser selectUserByEmail(String email);
    
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
    SysUser register(String firstName, String lastName, String email, String password, String recruiterId);
    
    /**
     * 发送密码重置邮件
     * 
     * @param email 邮箱
     */
    void sendPasswordResetEmail(String email);
}

