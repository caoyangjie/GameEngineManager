package com.gameengine.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.gameengine.system.domain.MemberTransaction;
import com.gameengine.system.domain.dto.MemberCenterResponse;
import com.gameengine.system.domain.dto.PurchaseRequest;
import com.gameengine.system.domain.dto.WithdrawalRequest;

/**
 * 会员服务 业务层
 * 
 * @author GameEngine
 */
public interface IMemberService {
    
    /**
     * 获取会员中心信息
     * 
     * @param userId 用户ID
     * @return 会员中心信息
     */
    MemberCenterResponse getMemberCenter(Long userId);
    
    /**
     * 购买虚拟货币
     * 
     * @param userId 用户ID
     * @param request 购买请求
     * @return 订单号
     */
    String purchaseCurrency(Long userId, PurchaseRequest request);
    
    /**
     * 提款
     * 
     * @param userId 用户ID
     * @param request 提款请求
     * @return 申请单号
     */
    String withdraw(Long userId, WithdrawalRequest request);
    
    /**
     * 收成（将赏金库转移到主金库）
     * 
     * @param userId 用户ID
     * @param autoAddJourney 是否自动追加旅程
     * @return 是否成功
     */
    Boolean harvest(Long userId, Boolean autoAddJourney);
    
    /**
     * 追加（将VT追加到倍增池）
     * 
     * @param userId 用户ID
     * @param vtAmount VT金额
     * @return 是否成功
     */
    Boolean addToMultiplierPool(Long userId, BigDecimal vtAmount);
    
    /**
     * 兑换（将VT兑换为USD）
     * 
     * @param userId 用户ID
     * @param vtAmount VT金额
     * @return 是否成功
     */
    Boolean exchange(Long userId, BigDecimal vtAmount);
    
    /**
     * 更新自动追加旅程设置
     * 
     * @param userId 用户ID
     * @param autoAddJourney 是否自动追加
     * @return 是否成功
     */
    Boolean updateAutoAddJourney(Long userId, Boolean autoAddJourney);
    
    /**
     * 获取交易记录列表
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param transactionType 交易类型
     * @param walletType 钱包类型
     * @return 交易记录列表
     */
    List<MemberTransaction> getTransactionHistory(Long userId, String startDate, String endDate, 
                                                   String transactionType, String walletType);
    
    /**
     * 检查并升级用户等级
     * 
     * @param userId 用户ID
     * @return 是否升级
     */
    Boolean checkAndUpgradeLevel(Long userId);
}

