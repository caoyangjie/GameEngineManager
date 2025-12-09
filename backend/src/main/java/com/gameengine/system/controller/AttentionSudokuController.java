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
import com.gameengine.system.domain.AttentionSudokuRecord;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.service.IAttentionSudokuService;
import com.gameengine.system.service.ISysUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数独游戏控制器
 * 
 * @author GameEngine
 */
@Tag(name = "数独游戏", description = "专注力训练-数独游戏相关功能")
@RestController
@RequestMapping("/attention/sudoku/records")
public class AttentionSudokuController extends BaseController {
    
    @Autowired
    private IAttentionSudokuService attentionSudokuService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private ISysUserService userService;
    
    /**
     * 保存游戏记录
     * 
     * @param record 游戏记录
     * @param request HTTP请求
     * @return 保存结果
     */
    @Operation(summary = "保存游戏记录", description = "保存数独游戏记录，包含数独尺寸、难度级别、用时等信息")
    @PostMapping
    public AjaxResult saveRecord(@RequestBody AttentionSudokuRecord record, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            
            // 忽略前端传递的userId，始终使用从token中获取的userId
            record.setUserId(userId);
            
            // 根据userId获取用户名
            SysUser user = userService.selectUserById(userId);
            if (user != null) {
                // 优先使用昵称，如果没有则使用用户名
                String username = user.getNickName();
                if (username == null || username.trim().isEmpty()) {
                    username = user.getUserName();
                }
                record.setUsername(username != null ? username : "匿名用户");
            } else {
                record.setUsername("匿名用户");
            }
            
            // 验证必填字段
            if (record.getGridSize() == null || record.getGridSize() <= 0) {
                return error("数独尺寸无效");
            }
            if (record.getDifficulty() == null || record.getDifficulty().trim().isEmpty()) {
                return error("难度级别无效");
            }
            if (record.getDurationMs() == null || record.getDurationMs() < 0) {
                return error("用时无效");
            }
            
            AttentionSudokuRecord savedRecord = attentionSudokuService.saveRecord(record);
            return success(savedRecord);
        } catch (Exception e) {
            logger.error("保存游戏记录失败", e);
            return error("保存游戏记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取TOP10最快记录
     * 
     * @param gridSize 数独尺寸，用于区分不同尺寸的排行榜（必填）
     * @param difficulty 难度级别，用于区分不同难度的排行榜（必填）
     * @param limit 返回记录数，默认10
     * @return TOP10记录列表
     */
    @Operation(summary = "获取TOP10最快记录", description = "根据数独尺寸和难度级别获取用时最短的前N条记录，按用时升序排列。每个尺寸和难度组合有独立的TOP10排行榜。")
    @GetMapping("/top")
    public AjaxResult getTopRecords(
            @RequestParam Integer gridSize,
            @RequestParam String difficulty,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            if (gridSize == null || gridSize <= 0) {
                return error("数独尺寸参数无效");
            }
            if (difficulty == null || difficulty.trim().isEmpty()) {
                return error("难度级别参数无效");
            }
            List<AttentionSudokuRecord> records = attentionSudokuService.getTopRecords(gridSize, difficulty, limit);
            return success(records);
        } catch (Exception e) {
            logger.error("获取TOP记录失败", e);
            return error("获取TOP记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 从请求中解析用户ID
     */
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

