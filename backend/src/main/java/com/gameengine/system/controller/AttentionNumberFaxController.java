package com.gameengine.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.AttentionNumberFaxRecord;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.service.IAttentionNumberFaxService;
import com.gameengine.system.service.ISysUserService;
import com.gameengine.system.service.impl.AudioStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数字传真训练控制器
 * 
 * @author GameEngine
 */
@Tag(name = "数字传真训练", description = "专注力训练-数字传真训练相关功能")
@RestController
@RequestMapping("/attention/numberFax/records")
public class AttentionNumberFaxController extends BaseController {
    
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Autowired
    private IAttentionNumberFaxService attentionNumberFaxService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private AudioStorageService audioStorageService;
    
    /**
     * 保存训练记录
     * 
     * @param faxRecord 训练记录
     * @param httpRequest HTTP请求
     * @return 保存结果
     */
    @Operation(summary = "保存训练记录", description = "保存数字传真训练记录，包含目标数字、数字组、语音包地址、答案等信息")
    @PostMapping
    public AjaxResult saveRecord(@RequestBody AttentionNumberFaxRecord faxRecord, HttpServletRequest httpRequest) {
        try {
            Long userId = getUserIdFromRequest(httpRequest);
            if (userId == null) {
                return error("未登录或token无效");
            }
            
            // 忽略前端传递的userId，始终使用从token中获取的userId
            faxRecord.setUserId(userId);
            
            // 根据userId获取用户名
            SysUser user = userService.selectUserById(userId);
            if (user != null) {
                // 优先使用昵称，如果没有则使用用户名
                String username = user.getNickName();
                if (username == null || username.trim().isEmpty()) {
                    username = user.getUserName();
                }
                faxRecord.setUsername(username != null ? username : "匿名用户");
            } else {
                faxRecord.setUsername("匿名用户");
            }
            
            // 验证必填字段
            if (faxRecord.getTargetNumber() == null) {
                return error("目标数字无效");
            }
            if (faxRecord.getGroupCount() == null || faxRecord.getGroupCount() <= 0) {
                return error("数字组数无效");
            }
            if (faxRecord.getGroupsJson() == null || faxRecord.getGroupsJson().trim().isEmpty()) {
                return error("数字组数据无效");
            }
            if (faxRecord.getCorrectAnswer() == null) {
                return error("正确答案无效");
            }
            
            // 如果前端传来音频内容（Base64 或 URL），在入库前先落盘到本地目录
            if (faxRecord.getAudioUrl() != null && !faxRecord.getAudioUrl().trim().isEmpty()) {
                String savedPath = audioStorageService.saveAudio(faxRecord.getAudioUrl(), "numberfax");
                faxRecord.setAudioUrl(savedPath);
            }
            
            AttentionNumberFaxRecord savedRecord = attentionNumberFaxService.saveRecord(faxRecord);
            return success(savedRecord);
        } catch (Exception e) {
            logger.error("保存训练记录失败", e);
            return error("保存训练记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取训练记录列表
     * 
     * @param limit 返回记录数，默认10
     * @return 训练记录列表
     */
    @Operation(summary = "获取训练记录列表", description = "获取最近的训练记录列表，按创建时间降序排列")
    @GetMapping
    public AjaxResult getRecords(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<AttentionNumberFaxRecord> records = attentionNumberFaxService.getRecords(limit);
            return success(records);
        } catch (Exception e) {
            logger.error("获取训练记录失败", e);
            return error("获取训练记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 从请求中解析用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }
        if (token == null) {
            return null;
        }
        return jwtUtils.getUserIdFromToken(token);
    }
}

