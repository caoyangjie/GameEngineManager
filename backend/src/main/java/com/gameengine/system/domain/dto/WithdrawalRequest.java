package com.gameengine.system.domain.dto;

import java.math.BigDecimal;

/**
 * 提款请求DTO
 * 
 * @author GameEngine
 */
public class WithdrawalRequest {
    
    /** 货币代码 */
    private String currencyCode;
    
    /** 提款金额 */
    private BigDecimal amount;
    
    /** 提款地址 */
    private String withdrawalAddress;
    
    /** 网络类型（BEP20, TRC20等） */
    private String networkType;
    
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
    
    public String getWithdrawalAddress() {
        return withdrawalAddress;
    }
    
    public void setWithdrawalAddress(String withdrawalAddress) {
        this.withdrawalAddress = withdrawalAddress;
    }
    
    public String getNetworkType() {
        return networkType;
    }
    
    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }
}

