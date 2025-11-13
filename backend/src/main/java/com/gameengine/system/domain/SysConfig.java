package com.gameengine.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gameengine.common.core.domain.BaseEntity;

/**
 * 系统配置对象 sys_config
 * 
 * @author GameEngine
 */
@TableName("sys_config")
public class SysConfig extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 配置ID */
    @TableId(type = IdType.AUTO)
    private Long configId;
    
    /** 配置键 */
    private String configKey;
    
    /** 配置值 */
    private String configValue;
    
    /** 配置类型（string:字符串 number:数字 boolean:布尔） */
    private String configType;
    
    /** 配置描述 */
    private String configDesc;
    
    public Long getConfigId() {
        return configId;
    }
    
    public void setConfigId(Long configId) {
        this.configId = configId;
    }
    
    public String getConfigKey() {
        return configKey;
    }
    
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }
    
    public String getConfigValue() {
        return configValue;
    }
    
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    
    public String getConfigType() {
        return configType;
    }
    
    public void setConfigType(String configType) {
        this.configType = configType;
    }
    
    public String getConfigDesc() {
        return configDesc;
    }
    
    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }
}

