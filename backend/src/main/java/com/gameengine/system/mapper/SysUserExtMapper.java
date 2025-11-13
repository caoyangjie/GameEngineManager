package com.gameengine.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.SysUserExt;

/**
 * 用户扩展表 数据层
 * 
 * @author GameEngine
 */
public interface SysUserExtMapper extends BaseMapper<SysUserExt> {
    
    /**
     * 根据用户ID查询用户扩展信息
     * 
     * @param userId 用户ID
     * @return 用户扩展对象信息
     */
    SysUserExt selectByUserId(Long userId);
}

