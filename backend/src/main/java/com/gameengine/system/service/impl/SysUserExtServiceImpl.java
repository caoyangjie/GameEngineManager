package com.gameengine.system.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gameengine.system.domain.SysUserExt;
import com.gameengine.system.mapper.SysUserExtMapper;
import com.gameengine.system.service.ISysUserExtService;

/**
 * 用户扩展 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class SysUserExtServiceImpl implements ISysUserExtService {
    
    @Autowired
    private SysUserExtMapper userExtMapper;
    
    /**
     * 根据用户ID查询用户扩展信息
     * 
     * @param userId 用户ID
     * @return 用户扩展对象信息
     */
    @Override
    public SysUserExt selectByUserId(Long userId) {
        return userExtMapper.selectOne(
            new LambdaQueryWrapper<SysUserExt>()
                .eq(SysUserExt::getUserId, userId)
        );
    }
    
    /**
     * 保存或更新用户扩展信息
     * 
     * @param userExt 用户扩展信息
     * @return 结果
     */
    @Override
    public int saveOrUpdate(SysUserExt userExt) {
        SysUserExt existing = selectByUserId(userExt.getUserId());
        if (existing != null) {
            userExt.setExtId(existing.getExtId());
            userExt.setUpdateTime(new Date());
            return userExtMapper.updateById(userExt);
        } else {
            userExt.setCreateTime(new Date());
            userExt.setUpdateTime(new Date());
            return userExtMapper.insert(userExt);
        }
    }
    
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
    @Override
    public int updateUserExt(Long userId, String recruitmentLink, String currentLevel, String playerId, String bep20Address) {
        SysUserExt userExt = new SysUserExt();
        userExt.setUserId(userId);
        userExt.setRecruitmentLink(recruitmentLink);
        userExt.setCurrentLevel(currentLevel);
        userExt.setPlayerId(playerId);
        userExt.setBep20Address(bep20Address);
        return saveOrUpdate(userExt);
    }
}

