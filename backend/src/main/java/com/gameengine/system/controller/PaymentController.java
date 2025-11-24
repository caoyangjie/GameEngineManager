package com.gameengine.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.dto.PaymentRequest;
import com.gameengine.system.service.IPaymentService;
import org.thlws.payment.alipay.entity.response.*;
import org.thlws.payment.wechat.entity.response.*;
import org.thlws.payment.bestpay.entity.response.OrderResultResponse;
import org.thlws.payment.bestpay.entity.response.OrderRefundResponse;
import org.thlws.payment.bestpay.entity.response.OrderReverseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 支付操作控制器
 *
 * @author GameEngine
 */
@Tag(name = "支付管理", description = "支付相关功能")
@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    @Autowired
    private IPaymentService paymentService;

    /**
     * 支付宝-手机网页支付
     */
    @Operation(summary = "支付宝手机网页支付", description = "生成支付宝手机网页支付表单")
    @PostMapping("/alipay/mobile-site")
    public AjaxResult alipayMobileSite(@Valid @RequestBody PaymentRequest request) {
        try {
            String html = paymentService.alipayMobileSite(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getSubject(), 
                request.getBody() != null ? request.getBody() : request.getSubject(), 
                request.getTotalAmount()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("html", html);
            return success(result);
        } catch (Exception e) {
            logger.error("支付宝手机网页支付失败", e);
            return error("支付失败: " + e.getMessage());
        }
    }

    /**
     * 支付宝-普通网站支付
     */
    @Operation(summary = "支付宝普通网站支付", description = "生成支付宝普通网站支付表单")
    @PostMapping("/alipay/web-site")
    public AjaxResult alipayWebSite(@Valid @RequestBody PaymentRequest request) {
        try {
            String html = paymentService.alipayWebSite(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getSubject(), 
                request.getBody() != null ? request.getBody() : request.getSubject(), 
                request.getTotalAmount()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("html", html);
            return success(result);
        } catch (Exception e) {
            logger.error("支付宝普通网站支付失败", e);
            return error("支付失败: " + e.getMessage());
        }
    }

    /**
     * 支付宝-预创建订单（扫码支付）
     */
    @Operation(summary = "支付宝预创建订单", description = "生成支付宝扫码支付二维码")
    @PostMapping("/alipay/pre-create")
    public AjaxResult alipayPreCreate(@Valid @RequestBody PaymentRequest request) {
        try {
            AlipayQrcodeResponse response = paymentService.alipayPreCreate(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getSubject(), 
                request.getBody() != null ? request.getBody() : request.getSubject(), 
                request.getTotalAmount(), 
                request.getStoreId(), 
                request.getOperatorId()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("qrcode", response.getQrCode());
            result.put("message", response.getMsg() != null ? response.getMsg() : response.getDesc());
            return success(result);
        } catch (Exception e) {
            logger.error("支付宝预创建订单失败", e);
            return error("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 支付宝-订单查询
     */
    @Operation(summary = "支付宝订单查询", description = "查询支付宝订单状态")
    @PostMapping("/alipay/query")
    public AjaxResult alipayQuery(@Valid @RequestBody PaymentRequest request) {
        try {
            AlipayQueryResponse response = paymentService.alipayQuery(request.getConfigId(), request.getOutTradeNo());
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getMsg() != null ? response.getMsg() : response.getDesc());
            return success(result);
        } catch (Exception e) {
            logger.error("支付宝订单查询失败", e);
            return error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 支付宝-退款
     */
    @Operation(summary = "支付宝退款", description = "申请支付宝退款")
    @PostMapping("/alipay/refund")
    public AjaxResult alipayRefund(@Valid @RequestBody PaymentRequest request) {
        try {
            if (request.getRefundAmount() == null) {
                return error("退款金额不能为空");
            }
            AlipayRefundResponse response = paymentService.alipayRefund(
                request.getConfigId(), 
                request.getTradeNo(), 
                request.getOutTradeNo(), 
                request.getRefundAmount(), 
                request.getRefundReason() != null ? request.getRefundReason() : "退款"
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getMsg() != null ? response.getMsg() : response.getDesc());
            return success(result);
        } catch (Exception e) {
            logger.error("支付宝退款失败", e);
            return error("退款失败: " + e.getMessage());
        }
    }

    /**
     * 支付宝-刷卡支付
     */
    @Operation(summary = "支付宝刷卡支付", description = "支付宝当面付刷卡支付")
    @PostMapping("/alipay/pay")
    public AjaxResult alipayPay(@Valid @RequestBody PaymentRequest request) {
        try {
            if (request.getAuthCode() == null || request.getAuthCode().isEmpty()) {
                return error("授权码不能为空");
            }
            AlipayTradeResponse response = paymentService.alipayPay(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getSubject(), 
                request.getAuthCode(), 
                request.getTotalAmount(), 
                request.getStoreId(), 
                request.getOperatorId()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getMsg() != null ? response.getMsg() : response.getDesc());
            return success(result);
        } catch (Exception e) {
            logger.error("支付宝刷卡支付失败", e);
            return error("支付失败: " + e.getMessage());
        }
    }

    /**
     * 支付宝-订单取消
     */
    @Operation(summary = "支付宝订单取消", description = "取消支付宝订单")
    @PostMapping("/alipay/cancel")
    public AjaxResult alipayCancel(@Valid @RequestBody PaymentRequest request) {
        try {
            AlipayCancelResponse response = paymentService.alipayCancel(request.getConfigId(), request.getOutTradeNo());
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getMsg() != null ? response.getMsg() : response.getDesc());
            return success(result);
        } catch (Exception e) {
            logger.error("支付宝订单取消失败", e);
            return error("取消失败: " + e.getMessage());
        }
    }

    /**
     * 微信-统一下单
     */
    @Operation(summary = "微信统一下单", description = "生成微信支付订单")
    @PostMapping("/wechat/unified-order")
    public AjaxResult wechatUnifiedOrder(@Valid @RequestBody PaymentRequest request) {
        try {
            if (request.getTradeType() == null || request.getTradeType().isEmpty()) {
                return error("交易类型不能为空");
            }
            UnifiedOrderResponse response = paymentService.wechatUnifiedOrder(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getBody() != null ? request.getBody() : request.getSubject(), 
                request.getTotalAmount(), 
                request.getTradeType(), 
                request.getOpenid()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("codeUrl", response.getCodeUrl());
            result.put("prepayId", response.getPrepayId());
            result.put("message", response.getMessage());
            return success(result);
        } catch (Exception e) {
            logger.error("微信统一下单失败", e);
            return error("下单失败: " + e.getMessage());
        }
    }

    /**
     * 微信-订单查询
     */
    @Operation(summary = "微信订单查询", description = "查询微信订单状态")
    @PostMapping("/wechat/query")
    public AjaxResult wechatQuery(@Valid @RequestBody PaymentRequest request) {
        try {
            OrderQueryResponse response = paymentService.wechatOrderQuery(
                request.getConfigId(), 
                request.getTradeNo(), 
                request.getOutTradeNo()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getReturnMsg() != null ? response.getReturnMsg() : (response.getTradeStateDesc() != null ? response.getTradeStateDesc() : "查询成功"));
            result.put("tradeState", response.getTradeState());
            return success(result);
        } catch (Exception e) {
            logger.error("微信订单查询失败", e);
            return error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 微信-退款
     */
    @Operation(summary = "微信退款", description = "申请微信退款")
    @PostMapping("/wechat/refund")
    public AjaxResult wechatRefund(@Valid @RequestBody PaymentRequest request) {
        try {
            if (request.getOutRefundNo() == null || request.getOutRefundNo().isEmpty()) {
                return error("退款单号不能为空");
            }
            if (request.getTotalFee() == null || request.getRefundFee() == null) {
                return error("原订单金额和退款金额不能为空");
            }
            WechatRefundResponse response = paymentService.wechatRefund(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getOutRefundNo(), 
                request.getTotalFee(), 
                request.getRefundFee()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getReturnMsg() != null ? response.getReturnMsg() : (response.isSuccess() ? "退款成功" : "退款失败"));
            return success(result);
        } catch (Exception e) {
            logger.error("微信退款失败", e);
            return error("退款失败: " + e.getMessage());
        }
    }

    /**
     * 微信-刷卡支付
     */
    @Operation(summary = "微信刷卡支付", description = "微信支付刷卡支付")
    @PostMapping("/wechat/micro-pay")
    public AjaxResult wechatMicroPay(@Valid @RequestBody PaymentRequest request) {
        try {
            if (request.getAuthCode() == null || request.getAuthCode().isEmpty()) {
                return error("授权码不能为空");
            }
            WechatPayResponse response = paymentService.wechatMicroPay(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getBody() != null ? request.getBody() : request.getSubject(), 
                request.getTotalAmount(), 
                request.getAuthCode()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getReturnMsg() != null ? response.getReturnMsg() : (response.isSuccess() ? "支付成功" : "支付失败"));
            result.put("transactionId", response.getTransactionId());
            return success(result);
        } catch (Exception e) {
            logger.error("微信刷卡支付失败", e);
            return error("支付失败: " + e.getMessage());
        }
    }

    /**
     * 微信-订单关闭
     */
    @Operation(summary = "微信订单关闭", description = "关闭微信支付订单")
    @PostMapping("/wechat/close-order")
    public AjaxResult wechatCloseOrder(@Valid @RequestBody PaymentRequest request) {
        try {
            CloseOrderResponse response = paymentService.wechatCloseOrder(request.getConfigId(), request.getOutTradeNo());
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getReturnMsg() != null ? response.getReturnMsg() : (response.isSuccess() ? "订单关闭成功" : "订单关闭失败"));
            return success(result);
        } catch (Exception e) {
            logger.error("微信订单关闭失败", e);
            return error("关闭失败: " + e.getMessage());
        }
    }

    /**
     * 翼支付-扫码支付
     */
    @Operation(summary = "翼支付扫码支付", description = "翼支付条码支付")
    @PostMapping("/bestpay/barcode")
    public AjaxResult bestpayBarcode(@Valid @RequestBody PaymentRequest request) {
        try {
            if (request.getBarcode() == null || request.getBarcode().isEmpty()) {
                return error("条码不能为空");
            }
            if (request.getGoodsName() == null || request.getGoodsName().isEmpty()) {
                return error("商品名称不能为空");
            }
            OrderResultResponse response = paymentService.bestpayBarcode(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getBarcode(), 
                request.getTotalAmount(), 
                request.getGoodsName(), 
                request.getStoreId()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isPaySuccess());
            result.put("message", response.getResult() != null && response.getResult().getRespDesc() != null 
                ? response.getResult().getRespDesc() : response.getErrorMsg());
            return success(result);
        } catch (Exception e) {
            logger.error("翼支付扫码支付失败", e);
            return error("支付失败: " + e.getMessage());
        }
    }

    /**
     * 翼支付-订单查询
     */
    @Operation(summary = "翼支付订单查询", description = "查询翼支付订单状态")
    @PostMapping("/bestpay/query")
    public AjaxResult bestpayQuery(@Valid @RequestBody PaymentRequest request) {
        try {
            OrderResultResponse response = paymentService.bestpayQuery(request.getConfigId(), request.getOutTradeNo());
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isPaySuccess());
            result.put("message", response.getResult() != null && response.getResult().getRespDesc() != null 
                ? response.getResult().getRespDesc() : response.getErrorMsg());
            return success(result);
        } catch (Exception e) {
            logger.error("翼支付订单查询失败", e);
            return error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 翼支付-退款
     */
    @Operation(summary = "翼支付退款", description = "申请翼支付退款")
    @PostMapping("/bestpay/refund")
    public AjaxResult bestpayRefund(@Valid @RequestBody PaymentRequest request) {
        try {
            if (request.getRefundAmount() == null) {
                return error("退款金额不能为空");
            }
            OrderRefundResponse response = paymentService.bestpayRefund(
                request.getConfigId(), 
                request.getOutTradeNo(), 
                request.getRefundAmount()
            );
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getErrorMsg() != null ? response.getErrorMsg() : "退款成功");
            return success(result);
        } catch (Exception e) {
            logger.error("翼支付退款失败", e);
            return error("退款失败: " + e.getMessage());
        }
    }

    /**
     * 翼支付-撤销
     */
    @Operation(summary = "翼支付撤销", description = "撤销翼支付订单")
    @PostMapping("/bestpay/reverse")
    public AjaxResult bestpayReverse(@Valid @RequestBody PaymentRequest request) {
        try {
            OrderReverseResponse response = paymentService.bestpayReverse(request.getConfigId(), request.getOutTradeNo());
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getErrorMsg() != null ? response.getErrorMsg() : "撤销成功");
            return success(result);
        } catch (Exception e) {
            logger.error("翼支付撤销失败", e);
            return error("撤销失败: " + e.getMessage());
        }
    }
}

