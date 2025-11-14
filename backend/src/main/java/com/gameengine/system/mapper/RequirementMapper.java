package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.Requirement;

/**
 * 用户需求 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface RequirementMapper extends BaseMapper<Requirement> {
    
}

