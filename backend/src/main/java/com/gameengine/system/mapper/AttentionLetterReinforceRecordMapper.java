package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.AttentionLetterReinforceRecord;

/**
 * 字母巩固游戏记录 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface AttentionLetterReinforceRecordMapper extends BaseMapper<AttentionLetterReinforceRecord> {
    
}

