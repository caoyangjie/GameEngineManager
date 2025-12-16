package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.WritingTrainingErrorLog;

/**
 * 写作训练出题错误日志 数据层
 */
@Mapper
public interface WritingTrainingErrorLogMapper extends BaseMapper<WritingTrainingErrorLog> {
}
