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
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.LearningRecord;
import com.gameengine.system.domain.LearningMethodologyTemplate;
import com.gameengine.system.service.ILearningRecordService;
import com.gameengine.system.service.ILearningMethodologyTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 学习记录管理
 * 
 * @author GameEngine
 */
@Tag(name = "学习记录管理", description = "学习记录相关功能")
@RestController
@RequestMapping("/learning-record")
public class LearningRecordController extends BaseController {
    
    @Autowired
    private ILearningRecordService recordService;
    
    @Autowired
    private ILearningMethodologyTemplateService templateService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 查询学习记录列表（不分页）
     * 
     * @param templateId 模板ID
     * @param studentName 学生姓名（模糊查询）
     * @param learningTopic 学习主题（模糊查询）
     * @param request HTTP请求
     * @return 学习记录列表
     */
    @Operation(summary = "查询学习记录列表", description = "查询学习记录列表，支持模板ID、学生姓名和学习主题筛选")
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam(required = false) Long templateId,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String learningTopic,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            
            LearningRecord record = new LearningRecord();
            record.setTemplateId(templateId);
            record.setStudentName(studentName);
            record.setLearningTopic(learningTopic);
            
            java.util.List<LearningRecord> result = recordService.selectRecordList(record, userId);
            
            // 如果模板ID存在，为每个记录填充模板标题
            if (templateId != null) {
                LearningMethodologyTemplate template = templateService.selectTemplateById(templateId);
                if (template != null) {
                    String templateTitle = template.getTitle();
                    for (LearningRecord r : result) {
                        if (r.getTemplateTitle() == null) {
                            r.setTemplateTitle(templateTitle);
                        }
                    }
                }
            } else {
                // 如果没有指定模板ID，需要为每个记录填充模板标题
                for (LearningRecord r : result) {
                    if (r.getTemplateTitle() == null && r.getTemplateId() != null) {
                        LearningMethodologyTemplate template = templateService.selectTemplateById(r.getTemplateId());
                        if (template != null) {
                            r.setTemplateTitle(template.getTitle());
                        }
                    }
                }
            }
            
            return success(result);
        } catch (Exception e) {
            logger.error("查询学习记录列表失败", e);
            return error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询学习记录详情
     * 
     * @param recordId 记录ID
     * @param request HTTP请求
     * @return 学习记录详情
     */
    @Operation(summary = "查询学习记录详情", description = "根据ID查询学习记录详细信息")
    @GetMapping("/{recordId}")
    public AjaxResult getInfo(@PathVariable Long recordId, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            
            LearningRecord record = recordService.selectRecordById(recordId, userId);
            if (record == null) {
                return error("学习记录不存在");
            }
            
            // 填充模板标题
            if (record.getTemplateTitle() == null && record.getTemplateId() != null) {
                LearningMethodologyTemplate template = templateService.selectTemplateById(record.getTemplateId());
                if (template != null) {
                    record.setTemplateTitle(template.getTitle());
                }
            }
            
            return success(record);
        } catch (Exception e) {
            logger.error("查询学习记录详情失败", e);
            return error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增学习记录
     * 
     * @param request HTTP请求
     * @param record 学习记录对象
     * @return 结果
     */
    @Operation(summary = "新增学习记录", description = "创建新的学习记录")
    @PostMapping
    public AjaxResult add(HttpServletRequest request, @RequestBody LearningRecord record) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            
            // 验证必填字段
            if (record.getTemplateId() == null) {
                return error("模板ID不能为空");
            }
            if (record.getStudentName() == null || record.getStudentName().trim().isEmpty()) {
                return error("学生姓名不能为空");
            }
            if (record.getLearningTopic() == null || record.getLearningTopic().trim().isEmpty()) {
                return error("学习主题不能为空");
            }
            
            // 验证模板是否存在
            LearningMethodologyTemplate template = templateService.selectTemplateById(record.getTemplateId());
            if (template == null) {
                return error("模板不存在");
            }
            
            // 设置用户ID和模板标题
            record.setUserId(userId);
            record.setTemplateTitle(template.getTitle());
            
            int result = recordService.insertRecord(record);
            return result > 0 ? success(record) : error("创建失败");
        } catch (Exception e) {
            logger.error("新增学习记录失败", e);
            return error("创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改学习记录
     * 
     * @param request HTTP请求
     * @param record 学习记录对象
     * @return 结果
     */
    @Operation(summary = "修改学习记录", description = "更新学习记录信息")
    @PutMapping
    public AjaxResult edit(HttpServletRequest request, @RequestBody LearningRecord record) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            
            // 验证记录是否存在且属于当前用户
            LearningRecord existingRecord = recordService.selectRecordById(record.getRecordId(), userId);
            if (existingRecord == null) {
                return error("学习记录不存在或无权限");
            }
            
            // 验证必填字段
            if (record.getStudentName() == null || record.getStudentName().trim().isEmpty()) {
                return error("学生姓名不能为空");
            }
            if (record.getLearningTopic() == null || record.getLearningTopic().trim().isEmpty()) {
                return error("学习主题不能为空");
            }
            
            // 如果模板ID改变，更新模板标题
            if (record.getTemplateId() != null && !record.getTemplateId().equals(existingRecord.getTemplateId())) {
                LearningMethodologyTemplate template = templateService.selectTemplateById(record.getTemplateId());
                if (template == null) {
                    return error("模板不存在");
                }
                record.setTemplateTitle(template.getTitle());
            }
            
            // 确保用户ID不被修改
            record.setUserId(userId);
            
            int result = recordService.updateRecord(record);
            return result > 0 ? success("更新成功") : error("更新失败");
        } catch (Exception e) {
            logger.error("修改学习记录失败", e);
            return error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新学习记录的步骤数据
     * 
     * @param recordId 记录ID
     * @param stepsData 步骤数据（JSON字符串）
     * @param request HTTP请求
     * @return 结果
     */
    @Operation(summary = "更新学习记录的步骤数据", description = "更新指定学习记录的步骤数据（包括步骤、任务完成状态、笔记、任务记录等）")
    @PutMapping("/{recordId}/steps-data")
    public AjaxResult updateStepsData(
            @PathVariable Long recordId,
            @RequestBody java.util.Map<String, Object> requestBody,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            
            // 验证记录是否存在且属于当前用户
            LearningRecord existingRecord = recordService.selectRecordById(recordId, userId);
            if (existingRecord == null) {
                return error("学习记录不存在或无权限");
            }
            
            // 获取步骤数据
            Object stepsDataObj = requestBody.get("stepsData");
            String stepsData = null;
            if (stepsDataObj != null) {
                if (stepsDataObj instanceof String) {
                    stepsData = (String) stepsDataObj;
                } else {
                    // 如果是对象，转换为JSON字符串
                    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    stepsData = objectMapper.writeValueAsString(stepsDataObj);
                }
            }
            
            // 更新步骤数据
            existingRecord.setStepsData(stepsData);
            int result = recordService.updateRecord(existingRecord);
            return result > 0 ? success("更新成功") : error("更新失败");
        } catch (Exception e) {
            logger.error("更新学习记录步骤数据失败", e);
            return error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除学习记录
     * 
     * @param recordIds 记录ID数组
     * @param request HTTP请求
     * @return 结果
     */
    @Operation(summary = "删除学习记录", description = "批量删除学习记录")
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            
            int result = recordService.deleteRecordByIds(recordIds, userId);
            return result > 0 ? success("删除成功") : error("删除失败");
        } catch (Exception e) {
            logger.error("删除学习记录失败", e);
            return error("删除失败: " + e.getMessage());
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

