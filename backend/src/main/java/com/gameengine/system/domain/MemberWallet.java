package com.gameengine.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户钱包对象 member_wallet
 * 
 * @author GameEngine
 */
@TableName("member_wallet")
public class MemberWallet {
    
    private static final long serialVersionUID = 1L;
    
    /** 钱包ID */
    @TableId(type = IdType.AUTO)
    private Long walletId;
    
    /** 用户ID */
    private Long userId;
    
    /** 货币代码 */
    private String currencyCode;
    
    /** 余额 */
    private BigDecimal balance;
    
    /** 锁定余额 */
    private BigDecimal lockedBalance;
    
    /** 总充值 */
    private BigDecimal totalDeposit;
    
    /** 总提款 */
    private BigDecimal totalWithdrawal;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    public Long getWalletId() {
        return walletId;
    }
    
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public BigDecimal getLockedBalance() {
        return lockedBalance;
    }
    
    public void setLockedBalance(BigDecimal lockedBalance) {
        this.lockedBalance = lockedBalance;
    }
    
    public BigDecimal getTotalDeposit() {
        return totalDeposit;
    }
    
    public void setTotalDeposit(BigDecimal totalDeposit) {
        this.totalDeposit = totalDeposit;
    }
    
    public BigDecimal getTotalWithdrawal() {
        return totalWithdrawal;
    }
    
    public void setTotalWithdrawal(BigDecimal totalWithdrawal) {
        this.totalWithdrawal = totalWithdrawal;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

