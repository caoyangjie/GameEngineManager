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
import com.gameengine.system.domain.Requirement;
import com.gameengine.system.service.IRequirementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户需求管理
 * 
 * @author GameEngine
 */
@Tag(name = "用户需求管理", description = "用户需求相关功能")
@RestController
@RequestMapping("/requirement")
public class RequirementController extends BaseController {
    
    @Autowired
    private IRequirementService requirementService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 查询用户需求列表（分页）
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param personaId 用户画像ID
     * @param canvasId 画布ID
     * @param title 标题（模糊查询）
     * @return 用户需求列表
     */
    @Operation(summary = "查询用户需求列表", description = "分页查询用户需求列表，支持用户画像ID、画布ID和标题筛选")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long personaId,
            @RequestParam(required = false) Long canvasId,
            @RequestParam(required = false) String title) {
        try {
            Page<Requirement> page = new Page<>(pageNum, pageSize);
            Requirement requirement = new Requirement();
            requirement.setPersonaId(personaId);
            requirement.setCanvasId(canvasId);
            requirement.setTitle(title);
            
            IPage<Requirement> result = requirementService.selectRequirementList(page, requirement);
            return success(result);
        } catch (Exception e) {
            logger.error("查询用户需求列表失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询用户需求详情
     * 
     * @param requirementId 用户需求ID
     * @return 用户需求详情
     */
    @Operation(summary = "查询用户需求详情", description = "根据ID查询用户需求详细信息")
    @GetMapping("/{requirementId}")
    public AjaxResult getInfo(@PathVariable Long requirementId) {
        try {
            Requirement requirement = requirementService.selectRequirementById(requirementId);
            if (requirement == null) {
                return AjaxResult.error(404, "用户需求不存在");
            }
            return success(requirement);
        } catch (Exception e) {
            logger.error("查询用户需求详情失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增用户需求
     * 
     * @param request HTTP请求
     * @param requirement 用户需求对象
     * @return 结果
     */
    @Operation(summary = "新增用户需求", description = "创建新的用户需求")
    @PostMapping
    public AjaxResult add(HttpServletRequest request, @RequestBody Requirement requirement) {
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
            if (requirement.getPersonaId() == null) {
                return AjaxResult.error(400, "用户画像ID不能为空");
            }
            if (requirement.getCanvasId() == null) {
                return AjaxResult.error(400, "画布ID不能为空");
            }
            
            // 设置创建者
            requirement.setCreateBy(String.valueOf(userId));
            
            int result = requirementService.insertRequirement(requirement);
            return result > 0 ? success("创建成功") : error("创建失败");
        } catch (Exception e) {
            logger.error("新增用户需求失败", e);
            return AjaxResult.error(500, "创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改用户需求
     * 
     * @param request HTTP请求
     * @param requirement 用户需求对象
     * @return 结果
     */
    @Operation(summary = "修改用户需求", description = "更新用户需求信息")
    @PutMapping
    public AjaxResult edit(HttpServletRequest request, @RequestBody Requirement requirement) {
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
            if (requirement.getRequirementId() == null) {
                return AjaxResult.error(400, "用户需求ID不能为空");
            }
            
            // 设置更新者
            requirement.setUpdateBy(String.valueOf(userId));
            
            int result = requirementService.updateRequirement(requirement);
            return result > 0 ? success("更新成功") : error("更新失败");
        } catch (Exception e) {
            logger.error("修改用户需求失败", e);
            return AjaxResult.error(500, "更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除用户需求
     * 
     * @param requirementIds 用户需求ID数组
     * @return 结果
     */
    @Operation(summary = "删除用户需求", description = "批量删除用户需求")
    @DeleteMapping("/{requirementIds}")
    public AjaxResult remove(@PathVariable Long[] requirementIds) {
        try {
            int result = requirementService.deleteRequirementByIds(requirementIds);
            return result > 0 ? success("删除成功") : error("删除失败");
        } catch (Exception e) {
            logger.error("删除用户需求失败", e);
            return AjaxResult.error(500, "删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询父需求列表（用于需求拆分）
     * 
     * @param personaId 用户画像ID
     * @param canvasId 画布ID
     * @param excludeRequirementId 排除的需求ID（编辑时排除自己）
     * @return 父需求列表
     */
    @Operation(summary = "查询父需求列表", description = "查询可以作为父需求的需求列表（没有父需求的需求）")
    @GetMapping("/parent-list")
    public AjaxResult getParentList(
            @RequestParam(required = false) Long personaId,
            @RequestParam(required = false) Long canvasId,
            @RequestParam(required = false) Long excludeRequirementId) {
        try {
            java.util.List<Requirement> result = requirementService.selectParentRequirementList(personaId, canvasId, excludeRequirementId);
            return success(result);
        } catch (Exception e) {
            logger.error("查询父需求列表失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
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

