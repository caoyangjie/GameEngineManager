package com.gameengine.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.PaymentConfig;

/**
 * 支付配置Mapper
 *
 * @author GameEngine
 */
@Mapper
public interface PaymentConfigMapper extends BaseMapper<PaymentConfig> {

    /**
     * 根据支付类型和启用状态查询
     */
    List<PaymentConfig> findByPaymentTypeAndEnabled(@Param("paymentType") String paymentType, @Param("enabled") Integer enabled);
}

