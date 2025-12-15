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
import com.gameengine.system.domain.AttentionTextFocusContent;
import com.gameengine.system.domain.AttentionTextFocusRecord;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.domain.dto.TextFocusGenerateRequest;
import com.gameengine.system.service.IAttentionTextFocusService;
import com.gameengine.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "文字专注力训练", description = "生成短文与问答、保存训练记录")
@RestController
@RequestMapping("/attention/textFocus")
public class AttentionTextFocusController extends BaseController {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private IAttentionTextFocusService textFocusService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ISysUserService userService;

    @Operation(summary = "生成训练包", description = "调用 DeepSeek 一次性生成 10 份文字专注力训练内容并落库")
    @PostMapping("/contents/generate")
    public AjaxResult generate(@RequestBody TextFocusGenerateRequest request, HttpServletRequest httpRequest) {
        try {
            Long userId = getUserIdFromRequest(httpRequest);
            List<AttentionTextFocusContent> contents = textFocusService.generateContents(request, userId);
            return success(contents);
        } catch (Exception e) {
            log.error("生成文字专注力训练内容失败", e);
            return error("生成失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取最新训练内容", description = "按更新时间降序返回最近生成的训练内容，可按主题筛选")
    @GetMapping("/contents/latest")
    public AjaxResult latest(@RequestParam(defaultValue = "10") Integer limit, @RequestParam(required = false) String theme) {
        try {
            List<AttentionTextFocusContent> contents = textFocusService.getLatestContents(limit, theme);
            return success(contents);
        } catch (Exception e) {
            log.error("获取文字专注力训练内容失败", e);
            return error("获取失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存训练记录", description = "保存用户的答题情况、用时、正确率等信息")
    @PostMapping("/records")
    public AjaxResult saveRecord(@RequestBody AttentionTextFocusRecord record, HttpServletRequest request) {
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
            AttentionTextFocusRecord saved = textFocusService.saveRecord(record);
            return success(saved);
        } catch (Exception e) {
            log.error("保存文字专注力训练记录失败", e);
            return error("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取用户记录", description = "按用户 ID 返回最近的训练记录")
    @GetMapping("/records/user")
    public AjaxResult getUserRecords(@RequestParam(defaultValue = "10") Integer limit, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("用户未登录");
            }
            List<AttentionTextFocusRecord> records = textFocusService.getRecordsByUserId(userId, limit);
            return success(records);
        } catch (Exception e) {
            log.error("获取文字专注力训练记录失败", e);
            return error("获取失败：" + e.getMessage());
        }
    }

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

