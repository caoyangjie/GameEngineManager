package com.gameengine.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gameengine.system.domain.SysConfig;
import com.gameengine.system.mapper.SysConfigMapper;
import com.gameengine.system.service.ISysConfigService;

/**
 * 系统配置 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    
    @Autowired
    private SysConfigMapper configMapper;
    
    /**
     * 根据配置键查询配置值
     * 
     * @param configKey 配置键
     * @return 配置值
     */
    @Override
    public String selectConfigValueByKey(String configKey) {
        SysConfig config = selectConfigByKey(configKey);
        return config != null ? config.getConfigValue() : null;
    }
    
    /**
     * 根据配置键查询配置对象
     * 
     * @param configKey 配置键
     * @return 配置对象
     */
    @Override
    public SysConfig selectConfigByKey(String configKey) {
        return configMapper.selectOne(
            new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, configKey)
        );
    }
}

