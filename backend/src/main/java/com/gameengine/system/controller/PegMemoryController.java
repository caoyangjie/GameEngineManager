package com.gameengine.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.PegMemoryRecord;
import com.gameengine.system.domain.PegMemoryTemplate;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.service.IPegMemoryService;
import com.gameengine.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "定桩记忆法训练", description = "专注力训练-定桩记忆法训练")
@RestController
@RequestMapping("/attention/pegMemory")
public class PegMemoryController extends BaseController {
    
    @Autowired
    private IPegMemoryService pegMemoryService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private ISysUserService userService;
    
    @Operation(summary = "保存训练记录", description = "保存用户在定桩记忆训练中的成绩与详情")
    @PostMapping("/records")
    public AjaxResult saveRecord(@RequestBody PegMemoryRecord record, HttpServletRequest request) {
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
            PegMemoryRecord saved = pegMemoryService.saveRecord(record);
            return success(saved);
        } catch (Exception e) {
            logger.error("保存定桩记忆训练记录失败", e);
            return error("保存训练记录失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取排行榜", description = "按正确率降序返回定桩记忆训练的排行榜")
    @GetMapping("/records/top")
    public AjaxResult getTopRecords(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<PegMemoryRecord> records = pegMemoryService.getTopRecords(limit);
            return success(records);
        } catch (Exception e) {
            logger.error("获取定桩记忆排行榜失败", e);
            return error("获取排行榜失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取所有模板", description = "返回所有定桩记忆模板列表")
    @GetMapping("/templates")
    public AjaxResult getAllTemplates() {
        try {
            List<PegMemoryTemplate> templates = pegMemoryService.getAllTemplates();
            return success(templates);
        } catch (Exception e) {
            logger.error("获取定桩记忆模板失败", e);
            return error("获取模板列表失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "根据ID获取模板", description = "根据模板ID获取详细信息")
    @GetMapping("/templates/{id}")
    public AjaxResult getTemplateById(@PathVariable Long id) {
        try {
            PegMemoryTemplate template = pegMemoryService.getTemplateById(id);
            if (template == null) {
                return error("模板不存在");
            }
            return success(template);
        } catch (Exception e) {
            logger.error("获取定桩记忆模板失败", e);
            return error("获取模板失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "创建模板", description = "创建新的定桩记忆模板")
    @PostMapping("/templates")
    public AjaxResult createTemplate(@RequestBody PegMemoryTemplate template, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            PegMemoryTemplate saved = pegMemoryService.saveTemplate(template, userId);
            return success(saved);
        } catch (Exception e) {
            logger.error("创建定桩记忆模板失败", e);
            return error("创建模板失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "更新模板", description = "更新已有的定桩记忆模板")
    @PutMapping("/templates/{id}")
    public AjaxResult updateTemplate(@PathVariable Long id, @RequestBody PegMemoryTemplate template, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            template.setId(id);
            PegMemoryTemplate saved = pegMemoryService.saveTemplate(template, userId);
            return success(saved);
        } catch (Exception e) {
            logger.error("更新定桩记忆模板失败", e);
            return error("更新模板失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "删除模板", description = "删除指定的定桩记忆模板（仅创建者可删除）")
    @DeleteMapping("/templates/{id}")
    public AjaxResult deleteTemplate(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            boolean deleted = pegMemoryService.deleteTemplate(id, userId);
            if (deleted) {
                return success("删除成功");
            } else {
                return error("删除失败：模板不存在或无权限");
            }
        } catch (Exception e) {
            logger.error("删除定桩记忆模板失败", e);
            return error("删除模板失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取默认模板", description = "返回所有标记为默认的模板")
    @GetMapping("/templates/default")
    public AjaxResult getDefaultTemplates() {
        try {
            List<PegMemoryTemplate> templates = pegMemoryService.getDefaultTemplates();
            return success(templates);
        } catch (Exception e) {
            logger.error("获取默认模板失败", e);
            return error("获取默认模板失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "按分类获取模板", description = "根据分类获取模板列表")
    @GetMapping("/templates/byCategory")
    public AjaxResult getTemplatesByCategory(@RequestParam String category) {
        try {
            List<PegMemoryTemplate> templates = pegMemoryService.getTemplatesByCategory(category);
            return success(templates);
        } catch (Exception e) {
            logger.error("按分类获取模板失败", e);
            return error("获取模板失败：" + e.getMessage());
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

