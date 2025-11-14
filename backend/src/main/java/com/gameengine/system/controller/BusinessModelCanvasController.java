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
import com.gameengine.system.domain.BusinessModelCanvas;
import com.gameengine.system.service.IBusinessModelCanvasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 商业模式画布管理
 * 
 * @author GameEngine
 */
@Tag(name = "商业模式画布管理", description = "商业模式画布相关功能")
@RestController
@RequestMapping("/business-model-canvas")
public class BusinessModelCanvasController extends BaseController {
    
    @Autowired
    private IBusinessModelCanvasService canvasService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 查询商业模式画布列表（分页）
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param title 标题（模糊查询）
     * @param status 状态
     * @param searchValue 搜索值
     * @return 画布列表
     */
    @Operation(summary = "查询商业模式画布列表", description = "分页查询商业模式画布列表，支持标题和状态筛选")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchValue) {
        try {
            Page<BusinessModelCanvas> page = new Page<>(pageNum, pageSize);
            BusinessModelCanvas canvas = new BusinessModelCanvas();
            canvas.setTitle(title);
            canvas.setStatus(status);
            canvas.setSearchValue(searchValue);
            
            IPage<BusinessModelCanvas> result = canvasService.selectCanvasList(page, canvas);
            return success(result);
        } catch (Exception e) {
            logger.error("查询商业模式画布列表失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询商业模式画布详情
     * 
     * @param canvasId 画布ID
     * @return 画布详情
     */
    @Operation(summary = "查询商业模式画布详情", description = "根据ID查询商业模式画布详细信息")
    @GetMapping("/{canvasId}")
    public AjaxResult getInfo(@PathVariable Long canvasId) {
        try {
            BusinessModelCanvas canvas = canvasService.selectCanvasById(canvasId);
            if (canvas == null) {
                return AjaxResult.error(404, "画布不存在");
            }
            return success(canvas);
        } catch (Exception e) {
            logger.error("查询商业模式画布详情失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增商业模式画布
     * 
     * @param request HTTP请求
     * @param canvas 画布对象
     * @return 结果
     */
    @Operation(summary = "新增商业模式画布", description = "创建新的商业模式画布")
    @PostMapping
    public AjaxResult add(HttpServletRequest request, @RequestBody BusinessModelCanvas canvas) {
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
            if (canvas.getTitle() == null || canvas.getTitle().trim().isEmpty()) {
                return AjaxResult.error(400, "标题不能为空");
            }
            
            // 设置创建者
            canvas.setCreateBy(String.valueOf(userId));
            // 默认状态为正常
            if (canvas.getStatus() == null) {
                canvas.setStatus("0");
            }
            
            int result = canvasService.insertCanvas(canvas);
            return result > 0 ? success("创建成功") : error("创建失败");
        } catch (Exception e) {
            logger.error("新增商业模式画布失败", e);
            return AjaxResult.error(500, "创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改商业模式画布
     * 
     * @param request HTTP请求
     * @param canvas 画布对象
     * @return 结果
     */
    @Operation(summary = "修改商业模式画布", description = "更新商业模式画布信息")
    @PutMapping
    public AjaxResult edit(HttpServletRequest request, @RequestBody BusinessModelCanvas canvas) {
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
            if (canvas.getTitle() == null || canvas.getTitle().trim().isEmpty()) {
                return AjaxResult.error(400, "标题不能为空");
            }
            
            // 设置更新者
            canvas.setUpdateBy(String.valueOf(userId));
            
            int result = canvasService.updateCanvas(canvas);
            return result > 0 ? success("更新成功") : error("更新失败");
        } catch (Exception e) {
            logger.error("修改商业模式画布失败", e);
            return AjaxResult.error(500, "更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除商业模式画布
     * 
     * @param canvasIds 画布ID数组
     * @return 结果
     */
    @Operation(summary = "删除商业模式画布", description = "批量删除商业模式画布")
    @DeleteMapping("/{canvasIds}")
    public AjaxResult remove(@PathVariable Long[] canvasIds) {
        try {
            int result = canvasService.deleteCanvasByIds(canvasIds);
            return result > 0 ? success("删除成功") : error("删除失败");
        } catch (Exception e) {
            logger.error("删除商业模式画布失败", e);
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

