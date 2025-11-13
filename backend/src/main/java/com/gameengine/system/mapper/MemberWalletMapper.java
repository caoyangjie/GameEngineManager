package com.gameengine.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.MemberWallet;

/**
 * 用户钱包 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface MemberWalletMapper extends BaseMapper<MemberWallet> {
    
    /**
     * 根据用户ID和货币代码查询钱包
     * 
     * @param userId 用户ID
     * @param currencyCode 货币代码
     * @return 钱包对象
     */
    MemberWallet selectByUserIdAndCurrency(@Param("userId") Long userId, @Param("currencyCode") String currencyCode);
}

