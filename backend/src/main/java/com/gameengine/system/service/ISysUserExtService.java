package com.gameengine.system.service;

import com.gameengine.system.domain.SysUserExt;

/**
 * 用户扩展 业务层
 * 
 * @author GameEngine
 */
public interface ISysUserExtService {
    
    /**
     * 根据用户ID查询用户扩展信息
     * 
     * @param userId 用户ID
     * @return 用户扩展对象信息
     */
    SysUserExt selectByUserId(Long userId);
    
    /**
     * 保存或更新用户扩展信息
     * 
     * @param userExt 用户扩展信息
     * @return 结果
     */
    int saveOrUpdate(SysUserExt userExt);
    
    /**
     * 更新用户扩展信息
     * 
     * @param userId 用户ID
     * @param recruitmentLink 招聘链接
     * @param currentLevel 当前旅程等级
     * @param playerId 玩家ID
     * @param bep20Address BEP20地址
     * @return 结果
     */
    int updateUserExt(Long userId, String recruitmentLink, String currentLevel, String playerId, String bep20Address);
}

