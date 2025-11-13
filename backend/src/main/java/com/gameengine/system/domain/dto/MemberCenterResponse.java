package com.gameengine.system.domain.dto;

import java.math.BigDecimal;

/**
 * 会员中心响应DTO
 * 
 * @author GameEngine
 */
public class MemberCenterResponse {
    
    /** 用户ID */
    private Long userId;
    
    /** 当前等级ID */
    private Long currentLevelId;
    
    /** 当前等级名称 */
    private String levelName;
    
    /** 当前等级代码 */
    private String levelCode;
    
    /** 当前等级图标 */
    private String levelIcon;
    
    /** 当前等级颜色 */
    private String levelColor;
    
    /** 总充值金额（USD） */
    private BigDecimal totalDeposit;
    
    /** 总投资金额（USD） */
    private BigDecimal totalInvestment;
    
    /** 总提款金额（USD） */
    private BigDecimal totalWithdrawal;
    
    /** 总收益（USD） */
    private BigDecimal totalEarnings;
    
    /** USD余额 */
    private BigDecimal usdBalance;
    
    /** VT余额 */
    private BigDecimal vtBalance;
    
    /** UNIFI余额 */
    private BigDecimal unifiBalance;
    
    /** 赏金库总额 */
    private BigDecimal bountyTotal;
    
    /** 主金库余额 */
    private BigDecimal mainVaultBalance;
    
    /** 自动追加旅程 */
    private Boolean autoAddJourney;
    
    /** VT价格 */
    private BigDecimal vtPrice;
    
    /** UNIFI价格 */
    private BigDecimal unifiPrice;
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getCurrentLevelId() {
        return currentLevelId;
    }
    
    public void setCurrentLevelId(Long currentLevelId) {
        this.currentLevelId = currentLevelId;
    }
    
    public String getLevelName() {
        return levelName;
    }
    
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    
    public String getLevelCode() {
        return levelCode;
    }
    
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }
    
    public String getLevelIcon() {
        return levelIcon;
    }
    
    public void setLevelIcon(String levelIcon) {
        this.levelIcon = levelIcon;
    }
    
    public String getLevelColor() {
        return levelColor;
    }
    
    public void setLevelColor(String levelColor) {
        this.levelColor = levelColor;
    }
    
    public BigDecimal getTotalDeposit() {
        return totalDeposit;
    }
    
    public void setTotalDeposit(BigDecimal totalDeposit) {
        this.totalDeposit = totalDeposit;
    }
    
    public BigDecimal getTotalInvestment() {
        return totalInvestment;
    }
    
    public void setTotalInvestment(BigDecimal totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
    
    public BigDecimal getTotalWithdrawal() {
        return totalWithdrawal;
    }
    
    public void setTotalWithdrawal(BigDecimal totalWithdrawal) {
        this.totalWithdrawal = totalWithdrawal;
    }
    
    public BigDecimal getTotalEarnings() {
        return totalEarnings;
    }
    
    public void setTotalEarnings(BigDecimal totalEarnings) {
        this.totalEarnings = totalEarnings;
    }
    
    public BigDecimal getUsdBalance() {
        return usdBalance;
    }
    
    public void setUsdBalance(BigDecimal usdBalance) {
        this.usdBalance = usdBalance;
    }
    
    public BigDecimal getVtBalance() {
        return vtBalance;
    }
    
    public void setVtBalance(BigDecimal vtBalance) {
        this.vtBalance = vtBalance;
    }
    
    public BigDecimal getUnifiBalance() {
        return unifiBalance;
    }
    
    public void setUnifiBalance(BigDecimal unifiBalance) {
        this.unifiBalance = unifiBalance;
    }
    
    public BigDecimal getBountyTotal() {
        return bountyTotal;
    }
    
    public void setBountyTotal(BigDecimal bountyTotal) {
        this.bountyTotal = bountyTotal;
    }
    
    public BigDecimal getMainVaultBalance() {
        return mainVaultBalance;
    }
    
    public void setMainVaultBalance(BigDecimal mainVaultBalance) {
        this.mainVaultBalance = mainVaultBalance;
    }
    
    public Boolean getAutoAddJourney() {
        return autoAddJourney;
    }
    
    public void setAutoAddJourney(Boolean autoAddJourney) {
        this.autoAddJourney = autoAddJourney;
    }
    
    public BigDecimal getVtPrice() {
        return vtPrice;
    }
    
    public void setVtPrice(BigDecimal vtPrice) {
        this.vtPrice = vtPrice;
    }
    
    public BigDecimal getUnifiPrice() {
        return unifiPrice;
    }
    
    public void setUnifiPrice(BigDecimal unifiPrice) {
        this.unifiPrice = unifiPrice;
    }
}

