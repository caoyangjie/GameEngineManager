package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.AttentionSudokuRecord;

/**
 * 数独游戏记录 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface AttentionSudokuRecordMapper extends BaseMapper<AttentionSudokuRecord> {
    
}

