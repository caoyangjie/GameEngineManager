package com.gameengine.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 虚拟货币类型对象 currency_type
 * 
 * @author GameEngine
 */
@TableName("currency_type")
public class CurrencyType {
    
    private static final long serialVersionUID = 1L;
    
    /** 货币ID */
    @TableId(type = IdType.AUTO)
    private Long currencyId;
    
    /** 货币代码（USD, VT, UNIFI等） */
    private String currencyCode;
    
    /** 货币名称 */
    private String currencyName;
    
    /** 货币符号 */
    private String currencySymbol;
    
    /** 货币图标 */
    private String currencyIcon;
    
    /** 小数位数 */
    private Integer decimals;
    
    /** 是否可交易（0否 1是） */
    private Integer isTradable;
    
    /** 兑换汇率（相对于USD） */
    private BigDecimal exchangeRate;
    
    /** 状态（0正常 1停用） */
    private String status;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    public Long getCurrencyId() {
        return currencyId;
    }
    
    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public String getCurrencyName() {
        return currencyName;
    }
    
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
    
    public String getCurrencySymbol() {
        return currencySymbol;
    }
    
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
    
    public String getCurrencyIcon() {
        return currencyIcon;
    }
    
    public void setCurrencyIcon(String currencyIcon) {
        this.currencyIcon = currencyIcon;
    }
    
    public Integer getDecimals() {
        return decimals;
    }
    
    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }
    
    public Integer getIsTradable() {
        return isTradable;
    }
    
    public void setIsTradable(Integer isTradable) {
        this.isTradable = isTradable;
    }
    
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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

