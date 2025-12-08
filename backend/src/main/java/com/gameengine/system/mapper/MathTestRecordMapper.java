package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.MathTestRecord;

/**
 * 数学测试记录 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface MathTestRecordMapper extends BaseMapper<MathTestRecord> {
    
}

