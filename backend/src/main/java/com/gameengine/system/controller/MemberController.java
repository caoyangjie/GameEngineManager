package com.gameengine.system.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.common.utils.MessageUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.MemberTransaction;
import com.gameengine.system.domain.dto.MemberCenterResponse;
import com.gameengine.system.domain.dto.PurchaseRequest;
import com.gameengine.system.domain.dto.WithdrawalRequest;
import com.gameengine.system.service.IMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 会员管理
 * 
 * @author GameEngine
 */
@Tag(name = "会员管理", description = "会员体系相关功能")
@RestController
@RequestMapping("/member")
public class MemberController extends BaseController {
    
    @Autowired
    private IMemberService memberService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取会员中心信息
     * 
     * @param request HTTP请求
     * @return 会员中心信息
     */
    @Operation(summary = "获取会员中心信息", description = "获取用户等级、钱包余额、金库等信息")
    @GetMapping("/center")
    public AjaxResult getMemberCenter(HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            MemberCenterResponse response = memberService.getMemberCenter(userId);
            return success(response);
        } catch (Exception e) {
            logger.error("获取会员中心信息失败", e);
            return AjaxResult.error(500, "获取会员中心信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 购买虚拟货币
     * 
     * @param request HTTP请求
     * @param purchaseRequest 购买请求
     * @return 订单号
     */
    @Operation(summary = "购买虚拟货币", description = "使用USD购买VT、UNIFI等虚拟货币")
    @PostMapping("/purchase")
    public AjaxResult purchaseCurrency(HttpServletRequest httpRequest, @Valid @RequestBody PurchaseRequest purchaseRequest) {
        String token = getToken(httpRequest);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            String orderNo = memberService.purchaseCurrency(userId, purchaseRequest);
            return success(orderNo);
        } catch (Exception e) {
            logger.error("购买虚拟货币失败", e);
            return AjaxResult.error(500, "购买失败: " + e.getMessage());
        }
    }
    
    /**
     * 提款
     * 
     * @param request HTTP请求
     * @param withdrawalRequest 提款请求
     * @return 申请单号
     */
    @Operation(summary = "提款", description = "申请提款到指定地址")
    @PostMapping("/withdraw")
    public AjaxResult withdraw(HttpServletRequest httpRequest, @Valid @RequestBody WithdrawalRequest withdrawalRequest) {
        String token = getToken(httpRequest);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            String requestNo = memberService.withdraw(userId, withdrawalRequest);
            return success(requestNo);
        } catch (Exception e) {
            logger.error("提款失败", e);
            return AjaxResult.error(500, "提款失败: " + e.getMessage());
        }
    }
    
    /**
     * 收成（将赏金库转移到主金库）
     * 
     * @param request HTTP请求
     * @param autoAddJourney 是否自动追加旅程
     * @return 是否成功
     */
    @Operation(summary = "收成", description = "将赏金库中的VT转移到主金库")
    @PostMapping("/harvest")
    public AjaxResult harvest(HttpServletRequest httpRequest, @RequestParam(required = false, defaultValue = "false") Boolean autoAddJourney) {
        String token = getToken(httpRequest);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            Boolean result = memberService.harvest(userId, autoAddJourney);
            return success(result);
        } catch (Exception e) {
            logger.error("收成失败", e);
            return AjaxResult.error(500, "收成失败: " + e.getMessage());
        }
    }
    
    /**
     * 追加（将VT追加到倍增池）
     * 
     * @param request HTTP请求
     * @param vtAmount VT金额
     * @return 是否成功
     */
    @Operation(summary = "追加", description = "将VT追加到倍增池")
    @PostMapping("/add")
    public AjaxResult addToMultiplierPool(HttpServletRequest httpRequest, @RequestParam BigDecimal vtAmount) {
        String token = getToken(httpRequest);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            Boolean result = memberService.addToMultiplierPool(userId, vtAmount);
            return success(result);
        } catch (Exception e) {
            logger.error("追加失败", e);
            return AjaxResult.error(500, "追加失败: " + e.getMessage());
        }
    }
    
    /**
     * 兑换（将VT兑换为USD）
     * 
     * @param request HTTP请求
     * @param vtAmount VT金额
     * @return 是否成功
     */
    @Operation(summary = "兑换", description = "将VT兑换为USD")
    @PostMapping("/exchange")
    public AjaxResult exchange(HttpServletRequest httpRequest, @RequestParam BigDecimal vtAmount) {
        String token = getToken(httpRequest);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            Boolean result = memberService.exchange(userId, vtAmount);
            return success(result);
        } catch (Exception e) {
            logger.error("兑换失败", e);
            return AjaxResult.error(500, "兑换失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新自动追加旅程设置
     * 
     * @param request HTTP请求
     * @param autoAddJourney 是否自动追加
     * @return 是否成功
     */
    @Operation(summary = "更新自动追加旅程设置", description = "设置是否自动将收成的VT追加到旅程")
    @PostMapping("/updateAutoAddJourney")
    public AjaxResult updateAutoAddJourney(HttpServletRequest httpRequest, @RequestParam Boolean autoAddJourney) {
        String token = getToken(httpRequest);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            Boolean result = memberService.updateAutoAddJourney(userId, autoAddJourney);
            return success(result);
        } catch (Exception e) {
            logger.error("更新自动追加旅程设置失败", e);
            return AjaxResult.error(500, "更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取交易记录
     * 
     * @param request HTTP请求
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param transactionType 交易类型
     * @param walletType 钱包类型
     * @return 交易记录列表
     */
    @Operation(summary = "获取交易记录", description = "查询用户的交易历史记录")
    @GetMapping("/transactions")
    public AjaxResult getTransactionHistory(HttpServletRequest httpRequest,
                                           @RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate,
                                           @RequestParam(required = false) String transactionType,
                                           @RequestParam(required = false) String walletType) {
        String token = getToken(httpRequest);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            List<MemberTransaction> transactions = memberService.getTransactionHistory(
                userId, startDate, endDate, transactionType, walletType);
            return success(transactions);
        } catch (Exception e) {
            logger.error("获取交易记录失败", e);
            return AjaxResult.error(500, "获取交易记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查并升级用户等级
     * 
     * @param request HTTP请求
     * @return 是否升级
     */
    @Operation(summary = "检查并升级用户等级", description = "根据充值金额和投资金额自动升级用户等级")
    @PostMapping("/checkLevel")
    public AjaxResult checkAndUpgradeLevel(HttpServletRequest httpRequest) {
        String token = getToken(httpRequest);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            Boolean upgraded = memberService.checkAndUpgradeLevel(userId);
            return success(upgraded);
        } catch (Exception e) {
            logger.error("检查等级失败", e);
            return AjaxResult.error(500, "检查等级失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取请求token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }
}

