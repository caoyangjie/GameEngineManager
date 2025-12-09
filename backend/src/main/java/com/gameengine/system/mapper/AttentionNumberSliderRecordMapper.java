package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.AttentionNumberSliderRecord;

/**
 * 数字华容道游戏记录 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface AttentionNumberSliderRecordMapper extends BaseMapper<AttentionNumberSliderRecord> {
    
}

