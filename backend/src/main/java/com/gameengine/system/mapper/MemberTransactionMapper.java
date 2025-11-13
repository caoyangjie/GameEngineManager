package com.gameengine.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.MemberTransaction;

/**
 * 会员交易记录 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface MemberTransactionMapper extends BaseMapper<MemberTransaction> {
    
    /**
     * 根据用户ID查询交易记录列表
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param transactionType 交易类型
     * @param walletType 钱包类型
     * @return 交易记录列表
     */
    List<MemberTransaction> selectTransactionsByUserId(
        @Param("userId") Long userId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("transactionType") String transactionType,
        @Param("walletType") String walletType
    );
}

