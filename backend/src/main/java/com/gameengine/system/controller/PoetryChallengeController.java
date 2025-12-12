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
import com.gameengine.system.domain.PoetryChallengeRecord;
import com.gameengine.system.domain.PoetryInfo;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.domain.dto.PoetryGenerateRequest;
import com.gameengine.system.service.IPoetryChallengeService;
import com.gameengine.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "诗词挑战记忆", description = "专注力训练-诗词挑战记忆训练")
@RestController
@RequestMapping("/attention/poetryChallenge")
public class PoetryChallengeController extends BaseController {
    
    @Autowired
    private IPoetryChallengeService poetryChallengeService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private ISysUserService userService;
    
    @Operation(summary = "生成诗词词单并落库", description = "调用 DeepSeek 生成诗词词单，保存到数据库并返回列表")
    @PostMapping("/poetries/generate")
    public AjaxResult generatePoetries(@RequestBody PoetryGenerateRequest request, HttpServletRequest httpServletRequest) {
        try {
            Long userId = getUserIdFromRequest(httpServletRequest);
            List<PoetryInfo> poetries = poetryChallengeService.generatePoetries(request, userId);
            return success(poetries);
        } catch (Exception e) {
            logger.error("生成诗词词单失败", e);
            return error("生成诗词词单失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取最新诗词词单", description = "按更新时间倒序获取最近生成的诗词资料")
    @GetMapping("/poetries/latest")
    public AjaxResult latestPoetries(@RequestParam(defaultValue = "8") Integer limit) {
        try {
            List<PoetryInfo> poetries = poetryChallengeService.getLatestPoetries(limit);
            return success(poetries);
        } catch (Exception e) {
            logger.error("获取最新诗词词单失败", e);
            return error("获取最新诗词词单失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "保存诗词记忆记录", description = "保存用户在诗词挑战记忆游戏中的成绩与详情")
    @PostMapping("/records")
    public AjaxResult saveRecord(@RequestBody PoetryChallengeRecord record, HttpServletRequest request) {
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
            PoetryChallengeRecord saved = poetryChallengeService.saveRecord(record);
            return success(saved);
        } catch (Exception e) {
            logger.error("保存诗词记忆记录失败", e);
            return error("保存诗词记忆记录失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取排行榜", description = "按正确率降序返回诗词记忆训练的排行榜")
    @GetMapping("/records/top")
    public AjaxResult getTopRecords(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<PoetryChallengeRecord> records = poetryChallengeService.getTopRecords(limit);
            return success(records);
        } catch (Exception e) {
            logger.error("获取诗词记忆排行榜失败", e);
            return error("获取诗词记忆排行榜失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取全部诗词标签", description = "返回去重后的诗词标签列表，用于前端分类展示")
    @GetMapping("/poetries/tags")
    public AjaxResult getAllTags() {
        try {
            List<String> tags = poetryChallengeService.getAllTags();
            return success(tags);
        } catch (Exception e) {
            logger.error("获取诗词标签失败", e);
            return error("获取诗词标签失败：" + e.getMessage());
        }
    }

    @Operation(summary = "按标签获取诗词", description = "根据标签从数据库查询已有诗词资料")
    @GetMapping("/poetries/byTag")
    public AjaxResult getPoetriesByTag(@RequestParam String tag, @RequestParam(required = false) Integer limit) {
        try {
            List<PoetryInfo> poetries = poetryChallengeService.getPoetriesByTag(tag, limit);
            return success(poetries);
        } catch (Exception e) {
            logger.error("按标签获取诗词失败", e);
            return error("按标签获取诗词失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取诗词视频讲解建议", description = "调用 DeepSeek 返回诗词讲解视频的创意/检索提示")
    @GetMapping("/poetries/videoSuggestion")
    public AjaxResult getVideoSuggestion(@RequestParam String title) {
        try {
            String suggestion = poetryChallengeService.getVideoSuggestion(title);
            return success(suggestion);
        } catch (Exception e) {
            logger.error("获取诗词视频建议失败", e);
            return error("获取诗词视频建议失败：" + e.getMessage());
        }
    }

    @Operation(summary = "检索诗词", description = "先从数据库查询诗词，没有则调用DeepSeek获取诗词资料信息（包括至少3条使用示例和视频地址）")
    @GetMapping("/poetries/search")
    public AjaxResult searchPoetry(@RequestParam String title) {
        try {
            PoetryInfo poetryInfo = poetryChallengeService.searchPoetry(title);
            return success(poetryInfo);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("检索诗词失败", e);
            return error("检索诗词失败：" + e.getMessage());
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

