package com.gameengine.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.WritingTrainingRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 写作训练记录 数据层
 */
@Mapper
public interface WritingTrainingRecordMapper extends BaseMapper<WritingTrainingRecord> {
}
