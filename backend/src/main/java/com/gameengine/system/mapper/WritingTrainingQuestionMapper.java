package com.gameengine.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.WritingTrainingQuestion;
import org.apache.ibatis.annotations.Mapper;

/**
 * 写作训练题目 数据层
 */
@Mapper
public interface WritingTrainingQuestionMapper extends BaseMapper<WritingTrainingQuestion> {
}
