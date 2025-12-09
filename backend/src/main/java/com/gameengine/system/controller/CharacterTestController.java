package com.gameengine.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.dto.CharacterTestDTO;
import com.gameengine.system.domain.dto.CharacterTestRecordDTO;
import com.gameengine.system.service.ICharacterTestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 识字测试控制器
 * 
 * @author GameEngine
 */
@Tag(name = "识字测试", description = "儿童识字测试表相关功能")
@RestController
@RequestMapping("/characterTest")
public class CharacterTestController extends BaseController {
    
    @Autowired
    private ICharacterTestService characterTestService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取测试汉字
     * 
     * @param educationLevel 教育阶段: primary(小学), middle(初中), high(高中)
     * @param grade 年级: 例如 primary-1 表示小学一年级
     * @param count 测试字数
     * @return 测试汉字列表
     */
    @Operation(summary = "获取测试汉字", description = "根据教育阶段和数量获取测试汉字")
    @GetMapping("/getCharacters")
    public AjaxResult getCharacters(
            @RequestParam(required = false, defaultValue = "primary") String educationLevel,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false, defaultValue = "50") Integer count) {
        try {
            List<CharacterTestDTO> characters = characterTestService.getTestCharacters(educationLevel, grade, count);
            return success(characters);
        } catch (Exception e) {
            logger.error("获取测试汉字失败", e);
            return error("获取测试汉字失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有汉字
     * 
     * @return 所有汉字列表
     */
    @Operation(summary = "获取所有汉字", description = "获取所有可用的汉字列表")
    @GetMapping("/getAllCharacters")
    public AjaxResult getAllCharacters() {
        try {
            List<CharacterTestDTO> characters = characterTestService.getAllCharacters();
            return success(characters);
        } catch (Exception e) {
            logger.error("获取所有汉字失败", e);
            return error("获取所有汉字失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存测试记录
     * 
     * @param recordDTO 测试记录DTO
     * @return 保存结果
     */
    @Operation(summary = "保存测试记录", description = "保存或更新测试记录")
    @PostMapping("/saveTestRecord")
    public AjaxResult saveTestRecord(@RequestBody CharacterTestRecordDTO recordDTO, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            CharacterTestRecordDTO savedRecord = characterTestService.saveTestRecord(recordDTO, userId);
            return success(savedRecord);
        } catch (Exception e) {
            logger.error("保存测试记录失败", e);
            return error("保存测试记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有测试记录
     * 
     * @return 测试记录列表
     */
    @Operation(summary = "获取所有测试记录", description = "获取所有测试记录列表")
    @GetMapping("/getAllTestRecords")
    public AjaxResult getAllTestRecords(HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            List<CharacterTestRecordDTO> records = characterTestService.getAllTestRecords(userId);
            return success(records);
        } catch (Exception e) {
            logger.error("获取测试记录失败", e);
            return error("获取测试记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取测试记录
     * 
     * @param id 测试记录ID
     * @return 测试记录
     */
    @Operation(summary = "获取测试记录", description = "根据ID获取测试记录")
    @GetMapping("/getTestRecord/{id}")
    public AjaxResult getTestRecordById(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            CharacterTestRecordDTO record = characterTestService.getTestRecordById(id, userId);
            if (record == null) {
                return error("测试记录不存在");
            }
            return success(record);
        } catch (Exception e) {
            logger.error("获取测试记录失败", e);
            return error("获取测试记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除测试记录
     * 
     * @param id 测试记录ID
     * @return 删除结果
     */
    @Operation(summary = "删除测试记录", description = "根据ID删除测试记录")
    @DeleteMapping("/deleteTestRecord/{id}")
    public AjaxResult deleteTestRecord(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            boolean success = characterTestService.deleteTestRecord(id, userId);
            if (success) {
                return success("删除成功");
            } else {
                return error("删除失败，记录不存在");
            }
        } catch (Exception e) {
            logger.error("删除测试记录失败", e);
            return error("删除测试记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询测试记录
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param studentName 学生姓名（模糊查询）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param minMasteryRate 最小掌握率
     * @param maxMasteryRate 最大掌握率
     * @return 分页结果
     */
    @Operation(summary = "分页查询测试记录", description = "分页查询测试记录，支持姓名、日期、掌握率筛选")
    @GetMapping("/getTestRecordsPage")
    public AjaxResult getTestRecordsPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer minMasteryRate,
            @RequestParam(required = false) Integer maxMasteryRate,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("未登录或token无效");
            }
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<CharacterTestRecordDTO> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
            
            com.baomidou.mybatisplus.core.metadata.IPage<CharacterTestRecordDTO> result = 
                characterTestService.getTestRecordsPage(page, studentName, startDate, endDate, minMasteryRate, maxMasteryRate, userId);
            
            return success(result);
        } catch (Exception e) {
            logger.error("分页查询测试记录失败", e);
            return error("分页查询测试记录失败: " + e.getMessage());
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

