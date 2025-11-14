package com.gameengine.system.controller;

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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.common.utils.MessageUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.Scenario;
import com.gameengine.system.service.IScenarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户场景管理
 * 
 * @author GameEngine
 */
@Tag(name = "用户场景管理", description = "用户场景相关功能")
@RestController
@RequestMapping("/scenario")
public class ScenarioController extends BaseController {
    
    @Autowired
    private IScenarioService scenarioService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 查询用户场景列表（分页）
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param personaId 用户画像ID
     * @param canvasId 画布ID
     * @param title 标题（模糊查询）
     * @return 用户场景列表
     */
    @Operation(summary = "查询用户场景列表", description = "分页查询用户场景列表，支持用户画像ID、画布ID和标题筛选")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long personaId,
            @RequestParam(required = false) Long canvasId,
            @RequestParam(required = false) String title) {
        try {
            Page<Scenario> page = new Page<>(pageNum, pageSize);
            Scenario scenario = new Scenario();
            scenario.setPersonaId(personaId);
            scenario.setCanvasId(canvasId);
            scenario.setTitle(title);
            
            IPage<Scenario> result = scenarioService.selectScenarioList(page, scenario);
            return success(result);
        } catch (Exception e) {
            logger.error("查询用户场景列表失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询用户场景详情
     * 
     * @param scenarioId 用户场景ID
     * @return 用户场景详情
     */
    @Operation(summary = "查询用户场景详情", description = "根据ID查询用户场景详细信息")
    @GetMapping("/{scenarioId}")
    public AjaxResult getInfo(@PathVariable Long scenarioId) {
        try {
            Scenario scenario = scenarioService.selectScenarioById(scenarioId);
            if (scenario == null) {
                return AjaxResult.error(404, "用户场景不存在");
            }
            return success(scenario);
        } catch (Exception e) {
            logger.error("查询用户场景详情失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增用户场景
     * 
     * @param request HTTP请求
     * @param scenario 用户场景对象
     * @return 结果
     */
    @Operation(summary = "新增用户场景", description = "创建新的用户场景")
    @PostMapping
    public AjaxResult add(HttpServletRequest request, @RequestBody Scenario scenario) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            // 验证必填字段
            if (scenario.getPersonaId() == null) {
                return AjaxResult.error(400, "用户画像ID不能为空");
            }
            if (scenario.getCanvasId() == null) {
                return AjaxResult.error(400, "画布ID不能为空");
            }
            
            // 设置创建者
            scenario.setCreateBy(String.valueOf(userId));
            
            int result = scenarioService.insertScenario(scenario);
            return result > 0 ? success("创建成功") : error("创建失败");
        } catch (Exception e) {
            logger.error("新增用户场景失败", e);
            return AjaxResult.error(500, "创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改用户场景
     * 
     * @param request HTTP请求
     * @param scenario 用户场景对象
     * @return 结果
     */
    @Operation(summary = "修改用户场景", description = "更新用户场景信息")
    @PutMapping
    public AjaxResult edit(HttpServletRequest request, @RequestBody Scenario scenario) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            // 验证必填字段
            if (scenario.getScenarioId() == null) {
                return AjaxResult.error(400, "用户场景ID不能为空");
            }
            
            // 设置更新者
            scenario.setUpdateBy(String.valueOf(userId));
            
            int result = scenarioService.updateScenario(scenario);
            return result > 0 ? success("更新成功") : error("更新失败");
        } catch (Exception e) {
            logger.error("修改用户场景失败", e);
            return AjaxResult.error(500, "更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除用户场景
     * 
     * @param scenarioIds 用户场景ID数组
     * @return 结果
     */
    @Operation(summary = "删除用户场景", description = "批量删除用户场景")
    @DeleteMapping("/{scenarioIds}")
    public AjaxResult remove(@PathVariable Long[] scenarioIds) {
        try {
            int result = scenarioService.deleteScenarioByIds(scenarioIds);
            return result > 0 ? success("删除成功") : error("删除失败");
        } catch (Exception e) {
            logger.error("删除用户场景失败", e);
            return AjaxResult.error(500, "删除失败: " + e.getMessage());
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

