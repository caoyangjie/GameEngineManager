package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.BusinessModelCanvas;

/**
 * 商业模式画布 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface BusinessModelCanvasMapper extends BaseMapper<BusinessModelCanvas> {
    
}

