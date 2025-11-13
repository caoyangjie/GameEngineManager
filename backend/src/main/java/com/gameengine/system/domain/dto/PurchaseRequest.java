package com.gameengine.system.domain.dto;

import java.math.BigDecimal;

/**
 * 购买请求DTO
 * 
 * @author GameEngine
 */
public class PurchaseRequest {
    
    /** 源货币代码 */
    private String fromCurrency;
    
    /** 目标货币代码 */
    private String toCurrency;
    
    /** 购买金额 */
    private BigDecimal amount;
    
    public String getFromCurrency() {
        return fromCurrency;
    }
    
    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }
    
    public String getToCurrency() {
        return toCurrency;
    }
    
    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

