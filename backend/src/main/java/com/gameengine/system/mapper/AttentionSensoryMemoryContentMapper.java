package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.AttentionSensoryMemoryContent;

/**
 * 感官记忆训练内容 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface AttentionSensoryMemoryContentMapper extends BaseMapper<AttentionSensoryMemoryContent> {
    
}

