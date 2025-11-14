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
import com.gameengine.system.domain.Persona;
import com.gameengine.system.service.IPersonaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户画像管理
 * 
 * @author GameEngine
 */
@Tag(name = "用户画像管理", description = "用户画像相关功能")
@RestController
@RequestMapping("/persona")
public class PersonaController extends BaseController {
    
    @Autowired
    private IPersonaService personaService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 查询用户画像列表（分页）
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param canvasId 画布ID
     * @param name 姓名（模糊查询）
     * @param gender 性别
     * @return 用户画像列表
     */
    @Operation(summary = "查询用户画像列表", description = "分页查询用户画像列表，支持画布ID、姓名和性别筛选")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long canvasId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender) {
        try {
            Page<Persona> page = new Page<>(pageNum, pageSize);
            Persona persona = new Persona();
            persona.setCanvasId(canvasId);
            persona.setName(name);
            persona.setGender(gender);
            
            IPage<Persona> result = personaService.selectPersonaList(page, persona);
            return success(result);
        } catch (Exception e) {
            logger.error("查询用户画像列表失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询用户画像详情
     * 
     * @param personaId 用户画像ID
     * @return 用户画像详情
     */
    @Operation(summary = "查询用户画像详情", description = "根据ID查询用户画像详细信息")
    @GetMapping("/{personaId}")
    public AjaxResult getInfo(@PathVariable Long personaId) {
        try {
            Persona persona = personaService.selectPersonaById(personaId);
            if (persona == null) {
                return AjaxResult.error(404, "用户画像不存在");
            }
            return success(persona);
        } catch (Exception e) {
            logger.error("查询用户画像详情失败", e);
            return AjaxResult.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增用户画像
     * 
     * @param request HTTP请求
     * @param persona 用户画像对象
     * @return 结果
     */
    @Operation(summary = "新增用户画像", description = "创建新的用户画像")
    @PostMapping
    public AjaxResult add(HttpServletRequest request, @RequestBody Persona persona) {
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
            if (persona.getCanvasId() == null) {
                return AjaxResult.error(400, "画布ID不能为空");
            }
            if (persona.getName() == null || persona.getName().trim().isEmpty()) {
                return AjaxResult.error(400, "姓名不能为空");
            }
            
            // 设置创建者
            persona.setCreateBy(String.valueOf(userId));
            
            int result = personaService.insertPersona(persona);
            return result > 0 ? success("创建成功") : error("创建失败");
        } catch (Exception e) {
            logger.error("新增用户画像失败", e);
            return AjaxResult.error(500, "创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改用户画像
     * 
     * @param request HTTP请求
     * @param persona 用户画像对象
     * @return 结果
     */
    @Operation(summary = "修改用户画像", description = "更新用户画像信息")
    @PutMapping
    public AjaxResult edit(HttpServletRequest request, @RequestBody Persona persona) {
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
            if (persona.getPersonaId() == null) {
                return AjaxResult.error(400, "用户画像ID不能为空");
            }
            if (persona.getName() == null || persona.getName().trim().isEmpty()) {
                return AjaxResult.error(400, "姓名不能为空");
            }
            
            // 设置更新者
            persona.setUpdateBy(String.valueOf(userId));
            
            int result = personaService.updatePersona(persona);
            return result > 0 ? success("更新成功") : error("更新失败");
        } catch (Exception e) {
            logger.error("修改用户画像失败", e);
            return AjaxResult.error(500, "更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除用户画像
     * 
     * @param personaIds 用户画像ID数组
     * @return 结果
     */
    @Operation(summary = "删除用户画像", description = "批量删除用户画像")
    @DeleteMapping("/{personaIds}")
    public AjaxResult remove(@PathVariable Long[] personaIds) {
        try {
            int result = personaService.deletePersonaByIds(personaIds);
            return result > 0 ? success("删除成功") : error("删除失败");
        } catch (Exception e) {
            logger.error("删除用户画像失败", e);
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

