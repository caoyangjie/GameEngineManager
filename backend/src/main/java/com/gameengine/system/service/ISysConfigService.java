package com.gameengine.system.service;

import com.gameengine.system.domain.SysConfig;

/**
 * 系统配置 业务层
 * 
 * @author GameEngine
 */
public interface ISysConfigService {
    
    /**
     * 根据配置键查询配置值
     * 
     * @param configKey 配置键
     * @return 配置值
     */
    String selectConfigValueByKey(String configKey);
    
    /**
     * 根据配置键查询配置对象
     * 
     * @param configKey 配置键
     * @return 配置对象
     */
    SysConfig selectConfigByKey(String configKey);
}

