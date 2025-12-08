package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.CharacterTestRecord;

/**
 * 识字测试记录 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface CharacterTestRecordMapper extends BaseMapper<CharacterTestRecord> {
    
}

