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
import com.gameengine.system.domain.AttentionSensoryMemoryContent;
import com.gameengine.system.domain.AttentionSensoryMemoryRecord;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.domain.dto.SensoryMemoryGenerateRequest;
import com.gameengine.system.service.ISensoryMemoryService;
import com.gameengine.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "感官探险记忆法", description = "专注力训练-感官探险记忆法训练")
@RestController
@RequestMapping("/attention/sensoryMemory")
public class SensoryMemoryController extends BaseController {
    
    @Autowired
    private ISensoryMemoryService sensoryMemoryService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private ISysUserService userService;
    
    @Operation(summary = "生成感官记忆内容并落库", description = "调用 DeepSeek 生成感官记忆内容，保存到数据库并返回列表")
    @PostMapping("/contents/generate")
    public AjaxResult generateContents(@RequestBody SensoryMemoryGenerateRequest request, HttpServletRequest httpServletRequest) {
        try {
            Long userId = getUserIdFromRequest(httpServletRequest);
            List<AttentionSensoryMemoryContent> contents = sensoryMemoryService.generateContents(request, userId);
            return success(contents);
        } catch (Exception e) {
            logger.error("生成感官记忆内容失败", e);
            return error("生成感官记忆内容失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取最新感官记忆内容", description = "按更新时间倒序获取最近生成的内容")
    @GetMapping("/contents/latest")
    public AjaxResult latestContents(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<AttentionSensoryMemoryContent> contents = sensoryMemoryService.getLatestContents(limit);
            return success(contents);
        } catch (Exception e) {
            logger.error("获取最新感官记忆内容失败", e);
            return error("获取最新感官记忆内容失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "保存感官记忆训练记录", description = "保存用户在感官记忆训练游戏中的成绩与详情")
    @PostMapping("/records")
    public AjaxResult saveRecord(@RequestBody AttentionSensoryMemoryRecord record, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId != null) {
                record.setUserId(userId);
                SysUser user = userService.selectUserById(userId);
                if (user != null) {
                    String username = user.getNickName();
                    if (username == null || username.trim().isEmpty()) {
                        username = user.getUserName();
                    }
                    record.setUsername(username != null ? username : "匿名用户");
                }
            }
            AttentionSensoryMemoryRecord saved = sensoryMemoryService.saveRecord(record);
            return success(saved);
        } catch (Exception e) {
            logger.error("保存感官记忆训练记录失败", e);
            return error("保存感官记忆训练记录失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取排行榜", description = "按正确率降序返回感官记忆训练的排行榜")
    @GetMapping("/records/top")
    public AjaxResult getTopRecords(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<AttentionSensoryMemoryRecord> records = sensoryMemoryService.getTopRecords(limit);
            return success(records);
        } catch (Exception e) {
            logger.error("获取感官记忆排行榜失败", e);
            return error("获取感官记忆排行榜失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取全部标签", description = "返回去重后的标签列表，用于前端分类展示")
    @GetMapping("/contents/tags")
    public AjaxResult getAllTags() {
        try {
            List<String> tags = sensoryMemoryService.getAllTags();
            return success(tags);
        } catch (Exception e) {
            logger.error("获取标签失败", e);
            return error("获取标签失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "按标签获取内容", description = "根据标签从数据库查询已有内容")
    @GetMapping("/contents/byTag")
    public AjaxResult getContentsByTag(@RequestParam String tag, @RequestParam(required = false) Integer limit) {
        try {
            List<AttentionSensoryMemoryContent> contents = sensoryMemoryService.getContentsByTag(tag, limit);
            return success(contents);
        } catch (Exception e) {
            logger.error("按标签获取内容失败", e);
            return error("按标签获取内容失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "分页查询用户记录", description = "按用户ID分页查询训练记录")
    @GetMapping("/records/list")
    public AjaxResult getRecordsByUserId(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("用户未登录");
            }
            List<AttentionSensoryMemoryRecord> records = sensoryMemoryService.getRecordsByUserId(userId, pageNum, pageSize);
            return success(records);
        } catch (Exception e) {
            logger.error("查询用户记录失败", e);
            return error("查询用户记录失败：" + e.getMessage());
        }
    }
    
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token == null) {
            return null;
        }
        return jwtUtils.getUserIdFromToken(token);
    }
}

