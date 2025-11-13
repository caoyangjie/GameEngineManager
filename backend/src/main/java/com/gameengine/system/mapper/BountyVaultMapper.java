package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.BountyVault;

/**
 * 赏金库 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface BountyVaultMapper extends BaseMapper<BountyVault> {
    
    /**
     * 根据用户ID和货币代码查询赏金库
     * 
     * @param userId 用户ID
     * @param currencyCode 货币代码
     * @return 赏金库对象
     */
    BountyVault selectByUserIdAndCurrency(@Param("userId") Long userId, @Param("currencyCode") String currencyCode);
}

