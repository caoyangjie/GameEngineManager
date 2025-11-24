package com.gameengine.system.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayConstants;
import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.system.domain.PaymentConfig;
import com.gameengine.system.domain.PaymentOrder;
import com.gameengine.system.mapper.PaymentConfigMapper;
import com.gameengine.system.mapper.PaymentOrderMapper;
import com.gameengine.system.service.IPaymentService;
import org.thlws.payment.AlipayClient;
import org.thlws.payment.BestPayClient;
import org.thlws.payment.WechatPayClient;
import org.thlws.payment.alipay.core.AlipayCore;
import org.thlws.payment.alipay.entity.request.*;
import org.thlws.payment.alipay.entity.response.*;
import org.thlws.payment.bestpay.entity.request.*;
import org.thlws.payment.bestpay.entity.response.*;
import org.thlws.payment.wechat.entity.request.*;
import org.thlws.payment.wechat.entity.response.*;

import lombok.extern.slf4j.Slf4j;

/**
 * 支付服务实现
 *
 * @author GameEngine
 */
@Slf4j
@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PaymentConfigMapper paymentConfigMapper;

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Override
    public String alipayMobileSite(Long configId, String outTradeNo, String subject, String body, BigDecimal totalAmount) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        AlipayCore alipayCore = buildAlipayCore(config);
        AlipayMobileSiteRequest request = new AlipayMobileSiteRequest();
        request.setNotifyUrl(config.getNotifyUrl());
        request.setReturnUrl(config.getReturnUrl());
        
        AlipayMobileSiteRequest.BizContent bizContent = new AlipayMobileSiteRequest.BizContent();
        bizContent.setTotalAmount(totalAmount.toString());
        bizContent.setSubject(subject);
        bizContent.setOutTradeNo(outTradeNo);
        request.setBizContent(bizContent);

        saveOrder(outTradeNo, "ALIPAY", "MOBILE_SITE", subject, body, totalAmount, configId);

        return AlipayClient.payInMobileSite(request, alipayCore);
    }

    @Override
    public String alipayWebSite(Long configId, String outTradeNo, String subject, String body, BigDecimal totalAmount) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        AlipayCore alipayCore = buildAlipayCore(config);
        AlipayWebSiteRequest request = new AlipayWebSiteRequest();
        request.setNotifyUrl(config.getNotifyUrl());
        request.setReturnUrl(config.getReturnUrl());
        
        AlipayWebSiteRequest.BizContent bizContent = new AlipayWebSiteRequest.BizContent();
        bizContent.setTotalAmount(totalAmount.toString());
        bizContent.setSubject(subject);
        bizContent.setBody(body);
        bizContent.setOutTradeNo(outTradeNo);
        request.setBizContent(bizContent);

        saveOrder(outTradeNo, "ALIPAY", "WEB_SITE", subject, body, totalAmount, configId);

        return AlipayClient.payInWebSite(request, alipayCore);
    }

    @Override
    public AlipayQrcodeResponse alipayPreCreate(Long configId, String outTradeNo, String subject, String body, 
                                                 BigDecimal totalAmount, String storeId, String operatorId) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        AlipayCore alipayCore = buildAlipayCore(config);
        AlipayQrcodeRequest request = new AlipayQrcodeRequest();
        request.setSubject(subject);
        request.setOutTradeNo(outTradeNo);
        request.setBody(body);
        request.setOperatorId(operatorId);
        request.setStoreId(storeId);
        request.setTotalAmount(totalAmount.toString());

        saveOrder(outTradeNo, "ALIPAY", "NATIVE", subject, body, totalAmount, configId);

        return AlipayClient.preCreate(request, alipayCore);
    }

    @Override
    public AlipayTradeResponse alipayPay(Long configId, String outTradeNo, String subject, String authCode, 
                                        BigDecimal totalAmount, String storeId, String operatorId) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        AlipayCore alipayCore = buildAlipayCore(config);
        AlipayTradeRequest request = new AlipayTradeRequest();
        request.setTotalAmount(totalAmount.toString());
        request.setStoreId(storeId);
        request.setOperatorId(operatorId);
        request.setAuthCode(authCode);
        request.setOutTradeNo(outTradeNo);
        request.setSubject(subject);

        saveOrder(outTradeNo, "ALIPAY", "MICROPAY", subject, null, totalAmount, configId);

        return AlipayClient.pay(request, alipayCore);
    }

    @Override
    public AlipayQueryResponse alipayQuery(Long configId, String outTradeNo) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        AlipayCore alipayCore = buildAlipayCore(config);
        return AlipayClient.query(outTradeNo, alipayCore);
    }

    @Override
    public AlipayRefundResponse alipayRefund(Long configId, String tradeNo, String outTradeNo, 
                                            BigDecimal refundAmount, String refundReason) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        AlipayCore alipayCore = buildAlipayCore(config);
        AlipayRefundRequest request = new AlipayRefundRequest();
        if (tradeNo != null && !tradeNo.isEmpty()) {
            request.setTradeNo(tradeNo);
        }
        if (outTradeNo != null && !outTradeNo.isEmpty()) {
            request.setOutTradeNo(outTradeNo);
        }
        request.setRefundAmount(refundAmount.toString());
        request.setRefundReason(refundReason);

        return AlipayClient.refund(request, alipayCore);
    }

    @Override
    public AlipayCancelResponse alipayCancel(Long configId, String outTradeNo) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        AlipayCore alipayCore = buildAlipayCore(config);
        return AlipayClient.cancel(outTradeNo, alipayCore);
    }

    @Override
    public UnifiedOrderResponse wechatUnifiedOrder(Long configId, String outTradeNo, String body, 
                                                   BigDecimal totalAmount, String tradeType, String openid) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        UnifiedOrderRequest request = new UnifiedOrderRequest();
        request.setAppId(config.getAppId());
        request.setMchId(config.getMchId());
        request.setBody(body);
        request.setOutTradeNo(outTradeNo);
        request.setTotalFee(String.valueOf(totalAmount.multiply(new BigDecimal("100")).intValue()));
        request.setTradeType(tradeType);
        request.setNotifyUrl(config.getNotifyUrl());
        request.setSpbillCreateIp("127.0.0.1");
        if (openid != null && !openid.isEmpty()) {
            request.setOpenId(openid);
        }

        saveOrder(outTradeNo, "WECHAT", tradeType, body, body, totalAmount, configId);

        return WechatPayClient.unifiedOrder(request, config.getApiKey());
    }

    @Override
    public WechatPayResponse wechatMicroPay(Long configId, String outTradeNo, String body, 
                                            BigDecimal totalAmount, String authCode) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        WechatPayRequest request = new WechatPayRequest();
        request.setAppId(config.getAppId());
        request.setMchId(config.getMchId());
        request.setSpbillCreateIp("127.0.0.1");
        request.setTotalFee(String.valueOf(totalAmount.multiply(new BigDecimal("100")).intValue()));
        request.setOutTradeNo(outTradeNo);
        request.setAuthCode(authCode);
        request.setBody(body);

        saveOrder(outTradeNo, "WECHAT", "MICROPAY", body, body, totalAmount, configId);

        return WechatPayClient.microPay(request, config.getApiKey());
    }

    @Override
    public OrderQueryResponse wechatOrderQuery(Long configId, String transactionId, String outTradeNo) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        OrderQueryRequest request = new OrderQueryRequest();
        request.setAppId(config.getAppId());
        request.setMchId(config.getMchId());
        if (transactionId != null && !transactionId.isEmpty()) {
            request.setTransactionId(transactionId);
        }
        if (outTradeNo != null && !outTradeNo.isEmpty()) {
            request.setOutTradeNo(outTradeNo);
        }

        return WechatPayClient.orderQuery(request, config.getApiKey());
    }

    @Override
    public WechatRefundResponse wechatRefund(Long configId, String outTradeNo, String outRefundNo, 
                                             BigDecimal totalFee, BigDecimal refundFee) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        if (config.getP12FilePath() == null || config.getP12FilePath().isEmpty()) {
            throw new ServiceException("微信退款需要配置P12证书文件路径");
        }

        WechatRefundRequest request = new WechatRefundRequest();
        request.setAppId(config.getAppId());
        request.setMchId(config.getMchId());
        request.setOutTradeNo(outTradeNo);
        request.setOutRefundNo(outRefundNo);
        request.setTotalFee(String.valueOf(totalFee.multiply(new BigDecimal("100")).intValue()));
        request.setRefundFee(String.valueOf(refundFee.multiply(new BigDecimal("100")).intValue()));

        return WechatPayClient.refund(request, config.getApiKey(), config.getP12FilePath());
    }

    @Override
    public CloseOrderResponse wechatCloseOrder(Long configId, String outTradeNo) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        CloseOrderRequest request = new CloseOrderRequest();
        request.setAppId(config.getAppId());
        request.setMchId(config.getMchId());
        request.setOutTradeNo(outTradeNo);

        return WechatPayClient.closeOrder(request, config.getApiKey());
    }

    @Override
    public OrderResultResponse bestpayBarcode(Long configId, String outTradeNo, String barcode, 
                                              BigDecimal totalAmount, String goodsName, String storeId) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        BarcodePayRequest request = new BarcodePayRequest();
        request.setMerchantId(config.getMchId());
        request.setBarcode(barcode);
        request.setOrderNo(outTradeNo);
        request.setOrderReqNo(outTradeNo);
        request.setOrderDate(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        request.setOrderAmt(totalAmount.toString());
        request.setProductAmt(totalAmount.toString());
        request.setGoodsName(goodsName);
        request.setStoreId(storeId);

        saveOrder(outTradeNo, "BESTPAY", "BARCODE", goodsName, goodsName, totalAmount, configId);

        return BestPayClient.barcode(request, config.getApiKey());
    }

    @Override
    public OrderResultResponse bestpayQuery(Long configId, String outTradeNo) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        QueryOrderRequest request = new QueryOrderRequest();
        request.setMerchantId(config.getMchId());
        request.setOrderNo(outTradeNo);
        request.setOrderReqNo(outTradeNo);
        request.setOrderDate(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        return BestPayClient.query(request, config.getApiKey());
    }

    @Override
    public OrderRefundResponse bestpayRefund(Long configId, String outTradeNo, BigDecimal refundAmount) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        OrderRefundRequest request = new OrderRefundRequest();
        request.setMerchantId(config.getMchId());
        request.setMerchantPwd(config.getApiKey());
        request.setOldOrderNo(outTradeNo);
        request.setOldOrderReqNo(outTradeNo);
        request.setRefundReqDate(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")));
        request.setRefundReqNo("REFUND" + System.currentTimeMillis());
        request.setTransAmt(refundAmount.multiply(new BigDecimal("100")).intValue() + "");

        return BestPayClient.refund(request, config.getApiKey());
    }

    @Override
    public OrderReverseResponse bestpayReverse(Long configId, String outTradeNo) throws Exception {
        PaymentConfig config = paymentConfigMapper.selectById(configId);
        if (config == null || config.getEnabled() != 1) {
            throw new ServiceException("支付配置不存在或未启用");
        }

        OrderReverseRequest request = new OrderReverseRequest();
        request.setMerchantId(config.getMchId());
        request.setMerchantPwd(config.getApiKey());
        request.setOldOrderNo(outTradeNo);
        request.setOldOrderReqNo(outTradeNo);
        request.setRefundReqDate(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")));
        request.setRefundReqNo("REVERSE" + System.currentTimeMillis());
        request.setTransAmt("0"); // TODO: 需要从数据库查询原订单金额

        return BestPayClient.reverse(request, config.getApiKey());
    }

    /**
     * 构建支付宝核心对象
     */
    private AlipayCore buildAlipayCore(PaymentConfig config) {
        AlipayCore.ClientBuilder builder = new AlipayCore.ClientBuilder();
        builder.setAppId(config.getAppId());
        builder.setPrivateKey(config.getPrivateKey());
        if (config.getAlipayPublicKey() != null && !config.getAlipayPublicKey().isEmpty()) {
            builder.setAlipayPublicKey(config.getAlipayPublicKey());
        }
        String signType = config.getSignType();
        if (signType == null || signType.isEmpty()) {
            signType = AlipayConstants.SIGN_TYPE_RSA2;
        }
        builder.setSignType(signType);
        return builder.build();
    }

    /**
     * 保存订单
     */
    private void saveOrder(String outTradeNo, String paymentType, String tradeType, 
                          String subject, String body, BigDecimal totalAmount, Long configId) {
        PaymentOrder order = new PaymentOrder();
        order.setOutTradeNo(outTradeNo);
        order.setPaymentType(paymentType);
        order.setTradeType(tradeType);
        order.setSubject(subject);
        order.setBody(body);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setConfigId(configId);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        paymentOrderMapper.insert(order);
    }
}

