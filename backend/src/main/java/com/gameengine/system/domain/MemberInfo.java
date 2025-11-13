package com.gameengine.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户会员信息对象 member_info
 * 
 * @author GameEngine
 */
@TableName("member_info")
public class MemberInfo {
    
    private static final long serialVersionUID = 1L;
    
    /** 会员ID */
    @TableId(type = IdType.AUTO)
    private Long memberId;
    
    /** 用户ID */
    private Long userId;
    
    /** 当前等级ID */
    private Long currentLevelId;
    
    /** 总充值金额（USD） */
    private BigDecimal totalDeposit;
    
    /** 总投资金额（USD） */
    private BigDecimal totalInvestment;
    
    /** 总提款金额（USD） */
    private BigDecimal totalWithdrawal;
    
    /** 总收益（USD） */
    private BigDecimal totalEarnings;
    
    /** 等级升级时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date levelUpTime;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    public Long getMemberId() {
        return memberId;
    }
    
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    
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
    
    public Date getLevelUpTime() {
        return levelUpTime;
    }
    
    public void setLevelUpTime(Date levelUpTime) {
        this.levelUpTime = levelUpTime;
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

