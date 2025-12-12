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
import com.gameengine.system.domain.AttentionIdiomAdvancedRecord;
import com.gameengine.system.domain.AttentionIdiomInfo;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.domain.dto.IdiomGenerateRequest;
import com.gameengine.system.service.IAttentionIdiomAdvancedService;
import com.gameengine.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "成语进阶记忆", description = "专注力训练-成语进阶记忆训练")
@RestController
@RequestMapping("/attention/idiomAdvanced")
public class AttentionIdiomAdvancedController extends BaseController {
    
    @Autowired
    private IAttentionIdiomAdvancedService idiomAdvancedService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private ISysUserService userService;
    
    @Operation(summary = "生成成语词单并落库", description = "调用 DeepSeek 生成成语词单，保存到数据库并返回列表")
    @PostMapping("/idioms/generate")
    public AjaxResult generateIdioms(@RequestBody IdiomGenerateRequest request, HttpServletRequest httpServletRequest) {
        try {
            Long userId = getUserIdFromRequest(httpServletRequest);
            List<AttentionIdiomInfo> idioms = idiomAdvancedService.generateIdioms(request, userId);
            return success(idioms);
        } catch (Exception e) {
            logger.error("生成成语词单失败", e);
            return error("生成成语词单失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取最新成语词单", description = "按更新时间倒序获取最近生成的成语资料")
    @GetMapping("/idioms/latest")
    public AjaxResult latestIdioms(@RequestParam(defaultValue = "8") Integer limit) {
        try {
            List<AttentionIdiomInfo> idioms = idiomAdvancedService.getLatestIdioms(limit);
            return success(idioms);
        } catch (Exception e) {
            logger.error("获取最新成语词单失败", e);
            return error("获取最新成语词单失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "保存成语记忆记录", description = "保存用户在成语进阶记忆游戏中的成绩与详情")
    @PostMapping("/records")
    public AjaxResult saveRecord(@RequestBody AttentionIdiomAdvancedRecord record, HttpServletRequest request) {
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
            AttentionIdiomAdvancedRecord saved = idiomAdvancedService.saveRecord(record);
            return success(saved);
        } catch (Exception e) {
            logger.error("保存成语记忆记录失败", e);
            return error("保存成语记忆记录失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取排行榜", description = "按正确率降序返回成语记忆训练的排行榜")
    @GetMapping("/records/top")
    public AjaxResult getTopRecords(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<AttentionIdiomAdvancedRecord> records = idiomAdvancedService.getTopRecords(limit);
            return success(records);
        } catch (Exception e) {
            logger.error("获取成语记忆排行榜失败", e);
            return error("获取成语记忆排行榜失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取全部成语标签", description = "返回去重后的成语标签列表，用于前端分类展示")
    @GetMapping("/idioms/tags")
    public AjaxResult getAllTags() {
        try {
            List<String> tags = idiomAdvancedService.getAllTags();
            return success(tags);
        } catch (Exception e) {
            logger.error("获取成语标签失败", e);
            return error("获取成语标签失败：" + e.getMessage());
        }
    }

    @Operation(summary = "按标签获取成语", description = "根据标签从数据库查询已有成语资料")
    @GetMapping("/idioms/byTag")
    public AjaxResult getIdiomsByTag(@RequestParam String tag, @RequestParam(required = false) Integer limit) {
        try {
            List<AttentionIdiomInfo> idioms = idiomAdvancedService.getIdiomsByTag(tag, limit);
            return success(idioms);
        } catch (Exception e) {
            logger.error("按标签获取成语失败", e);
            return error("按标签获取成语失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取成语视频讲解建议", description = "调用 DeepSeek 返回成语讲解视频的创意/检索提示")
    @GetMapping("/idioms/videoSuggestion")
    public AjaxResult getVideoSuggestion(@RequestParam String idiom) {
        try {
            String suggestion = idiomAdvancedService.getVideoSuggestion(idiom);
            return success(suggestion);
        } catch (Exception e) {
            logger.error("获取成语视频建议失败", e);
            return error("获取成语视频建议失败：" + e.getMessage());
        }
    }

    @Operation(summary = "检索成语", description = "先从数据库查询成语，没有则调用DeepSeek获取成语资料信息（包括至少3条使用示例和视频地址）")
    @GetMapping("/idioms/search")
    public AjaxResult searchIdiom(@RequestParam String idiom) {
        try {
            AttentionIdiomInfo idiomInfo = idiomAdvancedService.searchIdiom(idiom);
            return success(idiomInfo);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("检索成语失败", e);
            return error("检索成语失败：" + e.getMessage());
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


