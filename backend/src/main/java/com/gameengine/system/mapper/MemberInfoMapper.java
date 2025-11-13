package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.MemberInfo;

/**
 * 用户会员信息 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface MemberInfoMapper extends BaseMapper<MemberInfo> {
    
    /**
     * 根据用户ID查询会员信息
     * 
     * @param userId 用户ID
     * @return 会员信息
     */
    MemberInfo selectByUserId(Long userId);
}

