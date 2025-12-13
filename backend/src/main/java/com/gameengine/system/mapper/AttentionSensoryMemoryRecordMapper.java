package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.AttentionSensoryMemoryRecord;

/**
 * 感官探险记忆法训练记录 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface AttentionSensoryMemoryRecordMapper extends BaseMapper<AttentionSensoryMemoryRecord> {
    
}

