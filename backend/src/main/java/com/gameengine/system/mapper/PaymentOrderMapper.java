package com.gameengine.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.PaymentOrder;

/**
 * 支付订单Mapper
 *
 * @author GameEngine
 */
@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {

    /**
     * 根据商户订单号查询
     */
    PaymentOrder findByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    /**
     * 根据支付类型查询订单
     */
    List<PaymentOrder> findByPaymentType(@Param("paymentType") String paymentType);
}

