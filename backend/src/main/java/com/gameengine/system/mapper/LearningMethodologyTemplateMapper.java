package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.LearningMethodologyTemplate;

/**
 * 教育学习方法论模板 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface LearningMethodologyTemplateMapper extends BaseMapper<LearningMethodologyTemplate> {
    
}

