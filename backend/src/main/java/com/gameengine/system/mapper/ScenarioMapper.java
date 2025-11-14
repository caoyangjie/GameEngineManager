package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.Scenario;

/**
 * 用户场景 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface ScenarioMapper extends BaseMapper<Scenario> {
    
}

