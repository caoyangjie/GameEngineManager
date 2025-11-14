package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.Persona;

/**
 * 用户画像 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface PersonaMapper extends BaseMapper<Persona> {
    
}

