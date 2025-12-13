package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.AttentionNumberFaxRecord;

/**
 * 数字传真训练记录 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface AttentionNumberFaxRecordMapper extends BaseMapper<AttentionNumberFaxRecord> {
    
}

