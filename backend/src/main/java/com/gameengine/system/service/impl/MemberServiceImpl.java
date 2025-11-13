package com.gameengine.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.system.domain.BountyVault;
import com.gameengine.system.domain.CurrencyType;
import com.gameengine.system.domain.MainVault;
import com.gameengine.system.domain.MemberInfo;
import com.gameengine.system.domain.MemberLevel;
import com.gameengine.system.domain.MemberTransaction;
import com.gameengine.system.domain.MemberWallet;
import com.gameengine.system.domain.dto.MemberCenterResponse;
import com.gameengine.system.domain.dto.PurchaseRequest;
import com.gameengine.system.domain.dto.WithdrawalRequest;
import com.gameengine.system.mapper.BountyVaultMapper;
import com.gameengine.system.mapper.CurrencyTypeMapper;
import com.gameengine.system.mapper.MainVaultMapper;
import com.gameengine.system.mapper.MemberInfoMapper;
import com.gameengine.system.mapper.MemberLevelMapper;
import com.gameengine.system.mapper.MemberTransactionMapper;
import com.gameengine.system.mapper.MemberWalletMapper;
import com.gameengine.system.service.IMemberService;

/**
 * 会员服务 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class MemberServiceImpl implements IMemberService {
    
    @Autowired
    private MemberInfoMapper memberInfoMapper;
    
    @Autowired
    private MemberLevelMapper memberLevelMapper;
    
    @Autowired
    private MemberWalletMapper memberWalletMapper;
    
    @Autowired
    private BountyVaultMapper bountyVaultMapper;
    
    @Autowired
    private MainVaultMapper mainVaultMapper;
    
    @Autowired
    private CurrencyTypeMapper currencyTypeMapper;
    
    @Autowired
    private MemberTransactionMapper transactionMapper;
    
    @Override
    public MemberCenterResponse getMemberCenter(Long userId) {
        MemberCenterResponse response = new MemberCenterResponse();
        response.setUserId(userId);
        
        // 获取会员信息
        MemberInfo memberInfo = memberInfoMapper.selectByUserId(userId);
        if (memberInfo == null) {
            // 初始化会员信息
            memberInfo = initMemberInfo(userId);
        }
        
        // 获取等级信息
        if (memberInfo.getCurrentLevelId() != null) {
            MemberLevel level = memberLevelMapper.selectById(memberInfo.getCurrentLevelId());
            if (level != null) {
                response.setCurrentLevelId(level.getLevelId());
                response.setLevelName(level.getLevelName());
                response.setLevelCode(level.getLevelCode());
                response.setLevelIcon(level.getLevelIcon());
                response.setLevelColor(level.getLevelColor());
            }
        }
        
        response.setTotalDeposit(memberInfo.getTotalDeposit());
        response.setTotalInvestment(memberInfo.getTotalInvestment());
        response.setTotalWithdrawal(memberInfo.getTotalWithdrawal());
        response.setTotalEarnings(memberInfo.getTotalEarnings());
        
        // 获取钱包余额
        MemberWallet usdWallet = getOrCreateWallet(userId, "USD");
        MemberWallet vtWallet = getOrCreateWallet(userId, "VT");
        MemberWallet unifiWallet = getOrCreateWallet(userId, "UNIFI");
        
        response.setUsdBalance(usdWallet.getBalance());
        response.setVtBalance(vtWallet.getBalance());
        response.setUnifiBalance(unifiWallet.getBalance());
        
        // 获取赏金库
        BountyVault bountyVault = getOrCreateBountyVault(userId, "VT");
        response.setBountyTotal(bountyVault.getTotalBounty());
        response.setAutoAddJourney(bountyVault.getAutoAddJourney() == 1);
        
        // 获取主金库
        MainVault mainVault = getOrCreateMainVault(userId, "VT");
        response.setMainVaultBalance(mainVault.getBalance());
        
        // 获取货币价格
        CurrencyType vtCurrency = currencyTypeMapper.selectByCurrencyCode("VT");
        CurrencyType unifiCurrency = currencyTypeMapper.selectByCurrencyCode("UNIFI");
        
        if (vtCurrency != null) {
            response.setVtPrice(vtCurrency.getExchangeRate());
        }
        if (unifiCurrency != null) {
            response.setUnifiPrice(unifiCurrency.getExchangeRate());
        }
        
        return response;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String purchaseCurrency(Long userId, PurchaseRequest request) {
        // 验证货币类型
        CurrencyType fromCurrency = currencyTypeMapper.selectByCurrencyCode(request.getFromCurrency());
        CurrencyType toCurrency = currencyTypeMapper.selectByCurrencyCode(request.getToCurrency());
        
        if (fromCurrency == null || toCurrency == null) {
            throw new ServiceException("货币类型不存在");
        }
        
        if (fromCurrency.getIsTradable() == 0 || toCurrency.getIsTradable() == 0) {
            throw new ServiceException("货币不可交易");
        }
        
        // 获取源钱包
        MemberWallet fromWallet = getOrCreateWallet(userId, request.getFromCurrency());
        if (fromWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ServiceException("余额不足");
        }
        
        // 计算目标金额（使用汇率）
        BigDecimal exchangeRate = toCurrency.getExchangeRate().divide(fromCurrency.getExchangeRate(), 8, RoundingMode.HALF_UP);
        BigDecimal toAmount = request.getAmount().multiply(exchangeRate);
        
        // 扣除源货币
        BigDecimal newFromBalance = fromWallet.getBalance().subtract(request.getAmount());
        fromWallet.setBalance(newFromBalance);
        memberWalletMapper.updateById(fromWallet);
        
        // 增加目标货币
        MemberWallet toWallet = getOrCreateWallet(userId, request.getToCurrency());
        BigDecimal newToBalance = toWallet.getBalance().add(toAmount);
        toWallet.setBalance(newToBalance);
        memberWalletMapper.updateById(toWallet);
        
        // 记录交易
        recordTransaction(userId, "PURCHASE", "WALLET", "DEDUCT", 
                         request.getFromCurrency(), request.getAmount(), 
                         fromWallet.getBalance(), newFromBalance, exchangeRate);
        
        recordTransaction(userId, "PURCHASE", "WALLET", "RECHARGE", 
                         request.getToCurrency(), toAmount, 
                         toWallet.getBalance().subtract(toAmount), newToBalance, exchangeRate);
        
        // 生成订单号
        String orderNo = "PUR" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        return orderNo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String withdraw(Long userId, WithdrawalRequest request) {
        // 获取钱包
        MemberWallet wallet = getOrCreateWallet(userId, request.getCurrencyCode());
        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ServiceException("余额不足");
        }
        
        // 获取会员信息计算手续费
        MemberInfo memberInfo = memberInfoMapper.selectByUserId(userId);
        BigDecimal feeRate = BigDecimal.ZERO;
        if (memberInfo != null && memberInfo.getCurrentLevelId() != null) {
            MemberLevel level = memberLevelMapper.selectById(memberInfo.getCurrentLevelId());
            if (level != null) {
                feeRate = level.getWithdrawalFeeRate();
            }
        }
        
        BigDecimal fee = request.getAmount().multiply(feeRate);
        // BigDecimal actualAmount = request.getAmount().subtract(fee); // 实际到账金额，可用于后续处理
        
        // 扣除余额
        BigDecimal newBalance = wallet.getBalance().subtract(request.getAmount());
        wallet.setBalance(newBalance);
        wallet.setTotalWithdrawal(wallet.getTotalWithdrawal().add(request.getAmount()));
        memberWalletMapper.updateById(wallet);
        
        // 更新会员信息
        if (memberInfo != null) {
            memberInfo.setTotalWithdrawal(memberInfo.getTotalWithdrawal().add(request.getAmount()));
            memberInfoMapper.updateById(memberInfo);
        }
        
        // 记录交易
        recordTransaction(userId, "WITHDRAWAL", "WALLET", "DEDUCT", 
                         request.getCurrencyCode(), request.getAmount(), 
                         wallet.getBalance().add(request.getAmount()), newBalance, null);
        
        // 生成申请单号
        String requestNo = "WDR" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        return requestNo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean harvest(Long userId, Boolean autoAddJourney) {
        BountyVault bountyVault = getOrCreateBountyVault(userId, "VT");
        MainVault mainVault = getOrCreateMainVault(userId, "VT");
        
        if (bountyVault.getTotalBounty().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("赏金库余额为0");
        }
        
        BigDecimal bountyAmount = bountyVault.getTotalBounty();
        
        // 转移到主金库
        BigDecimal newMainBalance = mainVault.getBalance().add(bountyAmount);
        mainVault.setBalance(newMainBalance);
        mainVaultMapper.updateById(mainVault);
        
        // 清空赏金库
        BigDecimal oldBounty = bountyVault.getTotalBounty();
        bountyVault.setTotalBounty(BigDecimal.ZERO);
        bountyVault.setAutoAddJourney(autoAddJourney ? 1 : 0);
        bountyVaultMapper.updateById(bountyVault);
        
        // 记录交易
        recordTransaction(userId, "HARVEST", "BOUNTY_VAULT", "DEDUCT", 
                         "VT", bountyAmount, oldBounty, BigDecimal.ZERO, null);
        
        recordTransaction(userId, "HARVEST", "MAIN_VAULT", "RECHARGE", 
                         "VT", bountyAmount, mainVault.getBalance().subtract(bountyAmount), newMainBalance, null);
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addToMultiplierPool(Long userId, BigDecimal vtAmount) {
        MemberWallet vtWallet = getOrCreateWallet(userId, "VT");
        if (vtWallet.getBalance().compareTo(vtAmount) < 0) {
            throw new ServiceException("VT余额不足");
        }
        
        // 扣除VT
        BigDecimal newBalance = vtWallet.getBalance().subtract(vtAmount);
        vtWallet.setBalance(newBalance);
        memberWalletMapper.updateById(vtWallet);
        
        // 记录交易（追加到倍增池）
        recordTransaction(userId, "ADD", "VT_WALLET", "DEDUCT", 
                         "VT", vtAmount, vtWallet.getBalance().add(vtAmount), newBalance, null);
        
        // TODO: 这里应该调用倍增池相关的服务来增加倍增池值
        // 暂时只记录交易
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean exchange(Long userId, BigDecimal vtAmount) {
        MemberWallet vtWallet = getOrCreateWallet(userId, "VT");
        MemberWallet usdWallet = getOrCreateWallet(userId, "USD");
        
        if (vtWallet.getBalance().compareTo(vtAmount) < 0) {
            throw new ServiceException("VT余额不足");
        }
        
        // 获取VT价格
        CurrencyType vtCurrency = currencyTypeMapper.selectByCurrencyCode("VT");
        BigDecimal vtPrice = vtCurrency != null ? vtCurrency.getExchangeRate() : BigDecimal.ONE;
        
        // 计算USD金额
        BigDecimal usdAmount = vtAmount.multiply(vtPrice);
        
        // 扣除VT
        BigDecimal newVtBalance = vtWallet.getBalance().subtract(vtAmount);
        vtWallet.setBalance(newVtBalance);
        memberWalletMapper.updateById(vtWallet);
        
        // 增加USD
        BigDecimal newUsdBalance = usdWallet.getBalance().add(usdAmount);
        usdWallet.setBalance(newUsdBalance);
        memberWalletMapper.updateById(usdWallet);
        
        // 记录交易
        recordTransaction(userId, "EXCHANGE", "VT_WALLET", "DEDUCT", 
                         "VT", vtAmount, vtWallet.getBalance().add(vtAmount), newVtBalance, vtPrice);
        
        recordTransaction(userId, "EXCHANGE", "USD_WALLET", "RECHARGE", 
                         "USD", usdAmount, usdWallet.getBalance().subtract(usdAmount), newUsdBalance, vtPrice);
        
        return true;
    }
    
    @Override
    public Boolean updateAutoAddJourney(Long userId, Boolean autoAddJourney) {
        BountyVault bountyVault = getOrCreateBountyVault(userId, "VT");
        bountyVault.setAutoAddJourney(autoAddJourney ? 1 : 0);
        bountyVaultMapper.updateById(bountyVault);
        return true;
    }
    
    @Override
    public List<MemberTransaction> getTransactionHistory(Long userId, String startDate, String endDate, 
                                                          String transactionType, String walletType) {
        return transactionMapper.selectTransactionsByUserId(userId, startDate, endDate, transactionType, walletType);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkAndUpgradeLevel(Long userId) {
        MemberInfo memberInfo = memberInfoMapper.selectByUserId(userId);
        if (memberInfo == null) {
            return false;
        }
        
        // 查询所有等级，按排序升序
        List<MemberLevel> levels = memberLevelMapper.selectEnabledLevels();
        
        MemberLevel targetLevel = null;
        for (MemberLevel level : levels) {
            if (memberInfo.getTotalDeposit().compareTo(level.getMinTotalDeposit()) >= 0 &&
                memberInfo.getTotalInvestment().compareTo(level.getMinTotalInvestment()) >= 0) {
                targetLevel = level;
            } else {
                break;
            }
        }
        
        if (targetLevel != null && 
            (memberInfo.getCurrentLevelId() == null || 
             !targetLevel.getLevelId().equals(memberInfo.getCurrentLevelId()))) {
            memberInfo.setCurrentLevelId(targetLevel.getLevelId());
            memberInfo.setLevelUpTime(new Date());
            memberInfoMapper.updateById(memberInfo);
            return true;
        }
        
        return false;
    }
    
    // ========== 私有辅助方法 ==========
    
    private MemberInfo initMemberInfo(Long userId) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUserId(userId);
        memberInfo.setTotalDeposit(BigDecimal.ZERO);
        memberInfo.setTotalInvestment(BigDecimal.ZERO);
        memberInfo.setTotalWithdrawal(BigDecimal.ZERO);
        memberInfo.setTotalEarnings(BigDecimal.ZERO);
        memberInfo.setCreateTime(new Date());
        memberInfo.setUpdateTime(new Date());
        
        // 设置初始等级为最低等级
        List<MemberLevel> levels = memberLevelMapper.selectEnabledLevels();
        if (!levels.isEmpty()) {
            memberInfo.setCurrentLevelId(levels.get(0).getLevelId());
        }
        
        memberInfoMapper.insert(memberInfo);
        return memberInfo;
    }
    
    private MemberWallet getOrCreateWallet(Long userId, String currencyCode) {
        MemberWallet wallet = memberWalletMapper.selectByUserIdAndCurrency(userId, currencyCode);
        if (wallet == null) {
            wallet = new MemberWallet();
            wallet.setUserId(userId);
            wallet.setCurrencyCode(currencyCode);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setLockedBalance(BigDecimal.ZERO);
            wallet.setTotalDeposit(BigDecimal.ZERO);
            wallet.setTotalWithdrawal(BigDecimal.ZERO);
            wallet.setCreateTime(new Date());
            wallet.setUpdateTime(new Date());
            memberWalletMapper.insert(wallet);
        }
        return wallet;
    }
    
    private BountyVault getOrCreateBountyVault(Long userId, String currencyCode) {
        BountyVault vault = bountyVaultMapper.selectByUserIdAndCurrency(userId, currencyCode);
        if (vault == null) {
            vault = new BountyVault();
            vault.setUserId(userId);
            vault.setCurrencyCode(currencyCode);
            vault.setTotalBounty(BigDecimal.ZERO);
            vault.setAutoAddJourney(0);
            vault.setCreateTime(new Date());
            vault.setUpdateTime(new Date());
            bountyVaultMapper.insert(vault);
        }
        return vault;
    }
    
    private MainVault getOrCreateMainVault(Long userId, String currencyCode) {
        MainVault vault = mainVaultMapper.selectByUserIdAndCurrency(userId, currencyCode);
        if (vault == null) {
            vault = new MainVault();
            vault.setUserId(userId);
            vault.setCurrencyCode(currencyCode);
            vault.setBalance(BigDecimal.ZERO);
            vault.setCreateTime(new Date());
            vault.setUpdateTime(new Date());
            mainVaultMapper.insert(vault);
        }
        return vault;
    }
    
    private void recordTransaction(Long userId, String transactionType, String walletType, 
                                   String actionType, String currencyCode, BigDecimal amount,
                                   BigDecimal previousBalance, BigDecimal currentBalance, 
                                   BigDecimal exchangeRate) {
        MemberTransaction transaction = new MemberTransaction();
        transaction.setUserId(userId);
        transaction.setTransactionType(transactionType);
        transaction.setWalletType(walletType);
        transaction.setActionType(actionType);
        transaction.setCurrencyCode(currencyCode);
        transaction.setAmount(amount);
        transaction.setPreviousBalance(previousBalance);
        transaction.setCurrentBalance(currentBalance);
        transaction.setExchangeRate(exchangeRate);
        transaction.setCreateTime(new Date());
        transactionMapper.insert(transaction);
    }
}

