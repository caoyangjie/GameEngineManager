package com.gameengine.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 会员交易记录对象 member_transaction
 * 
 * @author GameEngine
 */
@TableName("member_transaction")
public class MemberTransaction {
    
    private static final long serialVersionUID = 1L;
    
    /** 交易ID */
    @TableId(type = IdType.AUTO)
    private Long transactionId;
    
    /** 用户ID */
    private Long userId;
    
    /** 交易类型（DEPOSIT:充值 WITHDRAWAL:提款 PURCHASE:购买 HARVEST:收成 ADD:追加 EXCHANGE:兑换等） */
    private String transactionType;
    
    /** 钱包类型（USD_WALLET:USD钱包 VT_WALLET:VT钱包 BOUNTY_VAULT:赏金库 MAIN_VAULT:主金库等） */
    private String walletType;
    
    /** 操作类型（RECHARGE:充值 DEDUCT:扣除） */
    private String actionType;
    
    /** 货币代码 */
    private String currencyCode;
    
    /** 交易金额 */
    private BigDecimal amount;
    
    /** 交易前余额 */
    private BigDecimal previousBalance;
    
    /** 交易后余额 */
    private BigDecimal currentBalance;
    
    /** 汇率（如VT价格） */
    private BigDecimal exchangeRate;
    
    /** 关联交易ID */
    private Long relatedTransactionId;
    
    /** 备注 */
    private String remark;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    public String getWalletType() {
        return walletType;
    }
    
    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }
    
    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }
    
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
    
    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
    
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    
    public Long getRelatedTransactionId() {
        return relatedTransactionId;
    }
    
    public void setRelatedTransactionId(Long relatedTransactionId) {
        this.relatedTransactionId = relatedTransactionId;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

