package com.gameengine.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 赏金库对象 bounty_vault
 * 
 * @author GameEngine
 */
@TableName("bounty_vault")
public class BountyVault {
    
    private static final long serialVersionUID = 1L;
    
    /** 金库ID */
    @TableId(type = IdType.AUTO)
    private Long vaultId;
    
    /** 用户ID */
    private Long userId;
    
    /** 货币代码 */
    private String currencyCode;
    
    /** 赏金总额 */
    private BigDecimal totalBounty;
    
    /** 自动追加旅程（0否 1是） */
    private Integer autoAddJourney;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    public Long getVaultId() {
        return vaultId;
    }
    
    public void setVaultId(Long vaultId) {
        this.vaultId = vaultId;
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
    
    public BigDecimal getTotalBounty() {
        return totalBounty;
    }
    
    public void setTotalBounty(BigDecimal totalBounty) {
        this.totalBounty = totalBounty;
    }
    
    public Integer getAutoAddJourney() {
        return autoAddJourney;
    }
    
    public void setAutoAddJourney(Integer autoAddJourney) {
        this.autoAddJourney = autoAddJourney;
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

