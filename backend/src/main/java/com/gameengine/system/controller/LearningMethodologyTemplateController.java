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
import com.gameengine.system.domain.LearningMethodologyTemplate;
import com.gameengine.system.service.ILearningMethodologyTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 教育学习方法论模板管理
 * 
 * @author GameEngine
 */
@Tag(name = "教育学习方法论模板管理", description = "教育学习方法论模板相关功能")
@RestController
@RequestMapping("/learning-methodology-template")
public class LearningMethodologyTemplateController extends BaseController {
    
    @Autowired
    private ILearningMethodologyTemplateService templateService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 查询模板列表（分页）
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param title 标题（模糊查询）
     * @param status 状态
     * @param searchValue 搜索值
     * @return 模板列表
     */
    @Operation(summary = "查询模板列表", description = "分页查询模板列表，支持标题和状态筛选")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchValue) {
        try {
            Page<LearningMethodologyTemplate> page = new Page<>(pageNum, pageSize);
            LearningMethodologyTemplate template = new LearningMethodologyTemplate();
            template.setTitle(title);
            template.setStatus(status);
            template.setSearchValue(searchValue);
            
            IPage<LearningMethodologyTemplate> result = templateService.selectTemplateList(page, template);
            return success(result);
        } catch (Exception e) {
            logger.error("查询模板列表失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询所有可用模板（不分页，用于下拉选择）
     * 
     * @return 模板列表
     */
    @Operation(summary = "查询所有可用模板", description = "查询所有状态为正常的模板，用于下拉选择")
    @GetMapping("/all")
    public AjaxResult getAll() {
        try {
            LearningMethodologyTemplate template = new LearningMethodologyTemplate();
            template.setStatus("0"); // 只查询正常状态的模板
            java.util.List<LearningMethodologyTemplate> result = templateService.selectTemplateList(template);
            return success(result);
        } catch (Exception e) {
            logger.error("查询所有模板失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询模板详情
     * 
     * @param templateId 模板ID
     * @return 模板详情
     */
    @Operation(summary = "查询模板详情", description = "根据ID查询模板详细信息")
    @GetMapping("/{templateId}")
    public AjaxResult getInfo(@PathVariable Long templateId) {
        try {
            LearningMethodologyTemplate template = templateService.selectTemplateById(templateId);
            if (template == null) {
                return AjaxResult.error(404, "模板不存在");
            }
            return success(template);
        } catch (Exception e) {
            logger.error("查询模板详情失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增模板
     * 
     * @param request HTTP请求
     * @param template 模板对象
     * @return 结果
     */
    @Operation(summary = "新增模板", description = "创建新的模板")
    @PostMapping
    public AjaxResult add(HttpServletRequest request, @RequestBody LearningMethodologyTemplate template) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            // 验证标题
            if (template.getTitle() == null || template.getTitle().trim().isEmpty()) {
                return AjaxResult.error(400, "标题不能为空");
            }
            
            // 验证步骤JSON
            if (template.getStepsJson() == null || template.getStepsJson().trim().isEmpty()) {
                return AjaxResult.error(400, "步骤模板JSON不能为空");
            }
            
            // 设置创建者
            template.setCreateBy(String.valueOf(userId));
            // 默认状态为正常
            if (template.getStatus() == null) {
                template.setStatus("0");
            }
            
            int result = templateService.insertTemplate(template);
            return result > 0 ? success("创建成功") : error("创建失败");
        } catch (Exception e) {
            logger.error("新增模板失败", e);
            return AjaxResult.error(500, "创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改模板
     * 
     * @param request HTTP请求
     * @param template 模板对象
     * @return 结果
     */
    @Operation(summary = "修改模板", description = "更新模板信息")
    @PutMapping
    public AjaxResult edit(HttpServletRequest request, @RequestBody LearningMethodologyTemplate template) {
        String token = getToken(request);
        if (token == null) {
            return AjaxResult.error(401, MessageUtils.message("not.login"));
        }
        
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return AjaxResult.error(401, MessageUtils.message("token.invalid"));
        }
        
        try {
            // 验证标题
            if (template.getTitle() == null || template.getTitle().trim().isEmpty()) {
                return AjaxResult.error(400, "标题不能为空");
            }
            
            // 验证步骤JSON
            if (template.getStepsJson() == null || template.getStepsJson().trim().isEmpty()) {
                return AjaxResult.error(400, "步骤模板JSON不能为空");
            }
            
            // 设置更新者
            template.setUpdateBy(String.valueOf(userId));
            
            int result = templateService.updateTemplate(template);
            return result > 0 ? success("更新成功") : error("更新失败");
        } catch (Exception e) {
            logger.error("修改模板失败", e);
            return AjaxResult.error(500, "更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除模板
     * 
     * @param templateIds 模板ID数组
     * @return 结果
     */
    @Operation(summary = "删除模板", description = "批量删除模板")
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds) {
        try {
            int result = templateService.deleteTemplateByIds(templateIds);
            return result > 0 ? success("删除成功") : error("删除失败");
        } catch (Exception e) {
            logger.error("删除模板失败", e);
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

