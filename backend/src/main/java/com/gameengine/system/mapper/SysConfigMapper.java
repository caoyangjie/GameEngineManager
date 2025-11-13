package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.SysConfig;

/**
 * 系统配置 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {
    
    /**
     * 根据配置键查询配置值
     * 
     * @param configKey 配置键
     * @return 配置对象
     */
    SysConfig selectByConfigKey(@Param("configKey") String configKey);
}

