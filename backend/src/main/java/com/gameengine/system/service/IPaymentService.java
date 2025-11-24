package com.gameengine.system.service;

import java.math.BigDecimal;

import org.thlws.payment.alipay.entity.response.*;
import org.thlws.payment.wechat.entity.response.*;
import org.thlws.payment.bestpay.entity.response.OrderResultResponse;
import org.thlws.payment.bestpay.entity.response.OrderRefundResponse;
import org.thlws.payment.bestpay.entity.response.OrderReverseResponse;

/**
 * 支付服务接口
 *
 * @author GameEngine
 */
public interface IPaymentService {

    // ========== 支付宝相关 ==========
    /**
     * 支付宝-手机网页支付
     */
    String alipayMobileSite(Long configId, String outTradeNo, String subject, String body, BigDecimal totalAmount) throws Exception;

    /**
     * 支付宝-普通网站支付
     */
    String alipayWebSite(Long configId, String outTradeNo, String subject, String body, BigDecimal totalAmount) throws Exception;

    /**
     * 支付宝-预创建订单（扫码支付）
     */
    AlipayQrcodeResponse alipayPreCreate(Long configId, String outTradeNo, String subject, String body, 
                                         BigDecimal totalAmount, String storeId, String operatorId) throws Exception;

    /**
     * 支付宝-刷卡支付
     */
    AlipayTradeResponse alipayPay(Long configId, String outTradeNo, String subject, String authCode, 
                                 BigDecimal totalAmount, String storeId, String operatorId) throws Exception;

    /**
     * 支付宝-订单查询
     */
    AlipayQueryResponse alipayQuery(Long configId, String outTradeNo) throws Exception;

    /**
     * 支付宝-退款
     */
    AlipayRefundResponse alipayRefund(Long configId, String tradeNo, String outTradeNo, 
                                     BigDecimal refundAmount, String refundReason) throws Exception;

    /**
     * 支付宝-订单取消
     */
    AlipayCancelResponse alipayCancel(Long configId, String outTradeNo) throws Exception;

    // ========== 微信相关 ==========
    /**
     * 微信-统一下单
     */
    UnifiedOrderResponse wechatUnifiedOrder(Long configId, String outTradeNo, String body, 
                                            BigDecimal totalAmount, String tradeType, String openid) throws Exception;

    /**
     * 微信-刷卡支付
     */
    WechatPayResponse wechatMicroPay(Long configId, String outTradeNo, String body, 
                                    BigDecimal totalAmount, String authCode) throws Exception;

    /**
     * 微信-订单查询
     */
    OrderQueryResponse wechatOrderQuery(Long configId, String transactionId, String outTradeNo) throws Exception;

    /**
     * 微信-退款
     */
    WechatRefundResponse wechatRefund(Long configId, String outTradeNo, String outRefundNo, 
                                     BigDecimal totalFee, BigDecimal refundFee) throws Exception;

    /**
     * 微信-订单关闭
     */
    CloseOrderResponse wechatCloseOrder(Long configId, String outTradeNo) throws Exception;

    // ========== 翼支付相关 ==========
    /**
     * 翼支付-扫码支付
     */
    OrderResultResponse bestpayBarcode(Long configId, String outTradeNo, String barcode, 
                                       BigDecimal totalAmount, String goodsName, String storeId) throws Exception;

    /**
     * 翼支付-订单查询
     */
    OrderResultResponse bestpayQuery(Long configId, String outTradeNo) throws Exception;

    /**
     * 翼支付-退款
     */
    OrderRefundResponse bestpayRefund(Long configId, String outTradeNo, BigDecimal refundAmount) throws Exception;

    /**
     * 翼支付-撤销
     */
    OrderReverseResponse bestpayReverse(Long configId, String outTradeNo) throws Exception;
}

