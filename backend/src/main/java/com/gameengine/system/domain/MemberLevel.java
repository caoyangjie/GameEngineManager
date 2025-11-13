package com.gameengine.system.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gameengine.common.core.domain.BaseEntity;

/**
 * 用户等级对象 member_level
 * 
 * @author GameEngine
 */
@TableName("member_level")
public class MemberLevel extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 等级ID */
    @TableId(type = IdType.AUTO)
    private Long levelId;
    
    /** 等级名称 */
    private String levelName;
    
    /** 等级代码（如：BRONZE, SILVER, GOLD等） */
    private String levelCode;
    
    /** 等级排序（数字越大等级越高） */
    private Integer levelSort;
    
    /** 最低总充值金额（USD） */
    private BigDecimal minTotalDeposit;
    
    /** 最低总投资金额（USD） */
    private BigDecimal minTotalInvestment;
    
    /** 每日赏金比例 */
    private BigDecimal dailyBountyRate;
    
    /** UNIFI分配比例 */
    private BigDecimal unifiAllocationRate;
    
    /** 提款手续费比例 */
    private BigDecimal withdrawalFeeRate;
    
    /** 等级图标 */
    private String levelIcon;
    
    /** 等级颜色 */
    private String levelColor;
    
    /** 等级描述 */
    private String levelDesc;
    
    /** 状态（0正常 1停用） */
    private String status;
    
    public Long getLevelId() {
        return levelId;
    }
    
    public void setLevelId(Long levelId) {
        this.levelId = levelId;
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
    
    public Integer getLevelSort() {
        return levelSort;
    }
    
    public void setLevelSort(Integer levelSort) {
        this.levelSort = levelSort;
    }
    
    public BigDecimal getMinTotalDeposit() {
        return minTotalDeposit;
    }
    
    public void setMinTotalDeposit(BigDecimal minTotalDeposit) {
        this.minTotalDeposit = minTotalDeposit;
    }
    
    public BigDecimal getMinTotalInvestment() {
        return minTotalInvestment;
    }
    
    public void setMinTotalInvestment(BigDecimal minTotalInvestment) {
        this.minTotalInvestment = minTotalInvestment;
    }
    
    public BigDecimal getDailyBountyRate() {
        return dailyBountyRate;
    }
    
    public void setDailyBountyRate(BigDecimal dailyBountyRate) {
        this.dailyBountyRate = dailyBountyRate;
    }
    
    public BigDecimal getUnifiAllocationRate() {
        return unifiAllocationRate;
    }
    
    public void setUnifiAllocationRate(BigDecimal unifiAllocationRate) {
        this.unifiAllocationRate = unifiAllocationRate;
    }
    
    public BigDecimal getWithdrawalFeeRate() {
        return withdrawalFeeRate;
    }
    
    public void setWithdrawalFeeRate(BigDecimal withdrawalFeeRate) {
        this.withdrawalFeeRate = withdrawalFeeRate;
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
    
    public String getLevelDesc() {
        return levelDesc;
    }
    
    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}

