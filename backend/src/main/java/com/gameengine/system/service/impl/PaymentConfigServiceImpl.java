package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameengine.system.domain.PaymentConfig;
import com.gameengine.system.mapper.PaymentConfigMapper;
import com.gameengine.system.service.IPaymentConfigService;

/**
 * 支付配置服务实现
 *
 * @author GameEngine
 */
@Service
public class PaymentConfigServiceImpl implements IPaymentConfigService {

    @Autowired
    private PaymentConfigMapper paymentConfigMapper;

    @Override
    public Long save(PaymentConfig config) {
        if (config.getId() == null) {
            config.setCreateTime(new Date());
            config.setUpdateTime(new Date());
            if (config.getEnabled() == null) {
                config.setEnabled(1);
            }
            paymentConfigMapper.insert(config);
            return config.getId();
        } else {
            config.setUpdateTime(new Date());
            paymentConfigMapper.updateById(config);
            return config.getId();
        }
    }

    @Override
    public PaymentConfig findById(Long id) {
        return paymentConfigMapper.selectById(id);
    }

    @Override
    public List<PaymentConfig> findAll() {
        return paymentConfigMapper.selectList(null);
    }

    @Override
    public List<PaymentConfig> findByPaymentTypeAndEnabled(String paymentType, Integer enabled) {
        return paymentConfigMapper.findByPaymentTypeAndEnabled(paymentType, enabled);
    }

    @Override
    public int deleteById(Long id) {
        return paymentConfigMapper.deleteById(id);
    }
}

