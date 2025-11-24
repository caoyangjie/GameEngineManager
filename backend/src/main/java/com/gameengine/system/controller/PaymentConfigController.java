package com.gameengine.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.PaymentConfig;
import com.gameengine.system.service.IPaymentConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 支付配置控制器
 *
 * @author GameEngine
 */
@Tag(name = "支付配置管理", description = "支付配置相关功能")
@RestController
@RequestMapping("/config/payment")
public class PaymentConfigController extends BaseController {

    @Autowired
    private IPaymentConfigService paymentConfigService;

    /**
     * 获取配置列表（API）
     */
    @Operation(summary = "获取支付配置列表", description = "获取所有支付配置或按类型筛选")
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(required = false) String paymentType) {
        try {
            List<PaymentConfig> configs;
            if (paymentType != null && !paymentType.isEmpty()) {
                configs = paymentConfigService.findByPaymentTypeAndEnabled(paymentType, 1);
            } else {
                configs = paymentConfigService.findAll();
            }
            return success(configs);
        } catch (Exception e) {
            logger.error("获取配置列表失败", e);
            return error("获取配置列表失败: " + e.getMessage());
        }
    }

    /**
     * 保存配置
     */
    @Operation(summary = "保存支付配置", description = "新增或更新支付配置")
    @PostMapping("/save")
    public AjaxResult save(@RequestBody PaymentConfig config) {
        try {
            if (config.getEnabled() == null) {
                config.setEnabled(1);
            }
            Long id = paymentConfigService.save(config);
            return success(id);
        } catch (Exception e) {
            logger.error("保存配置失败", e);
            return error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除配置
     */
    @Operation(summary = "删除支付配置", description = "根据ID删除支付配置")
    @PostMapping("/delete")
    public AjaxResult delete(@RequestParam Long id) {
        try {
            paymentConfigService.deleteById(id);
            return success();
        } catch (Exception e) {
            logger.error("删除配置失败", e);
            return error("删除失败: " + e.getMessage());
        }
    }
}

