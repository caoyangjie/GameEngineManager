package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.PaymentConfig;

/**
 * 支付配置服务接口
 *
 * @author GameEngine
 */
public interface IPaymentConfigService {

    /**
     * 保存配置
     */
    Long save(PaymentConfig config);

    /**
     * 根据ID查询
     */
    PaymentConfig findById(Long id);

    /**
     * 查询所有配置
     */
    List<PaymentConfig> findAll();

    /**
     * 根据支付类型查询启用的配置
     */
    List<PaymentConfig> findByPaymentTypeAndEnabled(String paymentType, Integer enabled);

    /**
     * 删除配置
     */
    int deleteById(Long id);
}

