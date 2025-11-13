package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.MainVault;

/**
 * 主金库 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface MainVaultMapper extends BaseMapper<MainVault> {
    
    /**
     * 根据用户ID和货币代码查询主金库
     * 
     * @param userId 用户ID
     * @param currencyCode 货币代码
     * @return 主金库对象
     */
    MainVault selectByUserIdAndCurrency(@Param("userId") Long userId, @Param("currencyCode") String currencyCode);
}

