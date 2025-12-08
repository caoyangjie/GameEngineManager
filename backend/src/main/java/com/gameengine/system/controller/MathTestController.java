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

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.dto.MathTestRecordDTO;
import com.gameengine.system.service.IMathTestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数学测试控制器
 * 
 * @author GameEngine
 */
@Tag(name = "数学测试", description = "小学生数学测试相关功能")
@RestController
@RequestMapping("/mathTest")
public class MathTestController extends BaseController {
    
    @Autowired
    private IMathTestService mathTestService;
    
    /**
     * 生成数学测试题目
     * 
     * @param operationTypes 计算类型列表（加减乘除），多个用逗号分隔
     * @param count 题目数量
     * @param minNumber 数字范围最小值
     * @param maxNumber 数字范围最大值
     * @return 测试题目列表
     */
    @Operation(summary = "生成数学测试题目", description = "根据计算类型、数量和数字范围生成测试题目")
    @GetMapping("/generateQuestions")
    public AjaxResult generateQuestions(
            @RequestParam String operationTypes,
            @RequestParam Integer count,
            @RequestParam Integer minNumber,
            @RequestParam Integer maxNumber) {
        try {
            // 将逗号分隔的字符串转换为列表
            List<String> types = List.of(operationTypes.split(","));
            List<MathTestRecordDTO.MathTestQuestionDTO> questions = 
                mathTestService.generateTestQuestions(types, count, minNumber, maxNumber);
            return success(questions);
        } catch (Exception e) {
            logger.error("生成测试题目失败", e);
            return error("生成测试题目失败: " + e.getMessage());
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
    public AjaxResult saveTestRecord(@RequestBody MathTestRecordDTO recordDTO) {
        try {
            // 计算正确率和统计信息
            if (recordDTO.getQuestions() != null && !recordDTO.getQuestions().isEmpty()) {
                int correctCount = 0;
                int incorrectCount = 0;
                
                for (MathTestRecordDTO.MathTestQuestionDTO question : recordDTO.getQuestions()) {
                    if (question.getStudentAnswer() != null) {
                        boolean isCorrect = question.getStudentAnswer().equals(question.getCorrectAnswer());
                        question.setIsCorrect(isCorrect);
                        if (isCorrect) {
                            correctCount++;
                        } else {
                            incorrectCount++;
                        }
                    }
                }
                
                recordDTO.setCorrectCount(correctCount);
                recordDTO.setIncorrectCount(incorrectCount);
                
                // 计算正确率
                int total = recordDTO.getQuestions().size();
                if (total > 0) {
                    int accuracyRate = Math.round((float) correctCount / total * 100);
                    recordDTO.setAccuracyRate(accuracyRate);
                } else {
                    recordDTO.setAccuracyRate(0);
                }
            }
            
            MathTestRecordDTO savedRecord = mathTestService.saveTestRecord(recordDTO);
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
    public AjaxResult getAllTestRecords() {
        try {
            List<MathTestRecordDTO> records = mathTestService.getAllTestRecords();
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
    public AjaxResult getTestRecordById(@PathVariable Long id) {
        try {
            MathTestRecordDTO record = mathTestService.getTestRecordById(id);
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
    public AjaxResult deleteTestRecord(@PathVariable Long id) {
        try {
            boolean success = mathTestService.deleteTestRecord(id);
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
     * @param minAccuracyRate 最小正确率
     * @param maxAccuracyRate 最大正确率
     * @return 分页结果
     */
    @Operation(summary = "分页查询测试记录", description = "分页查询测试记录，支持姓名、日期、正确率筛选")
    @GetMapping("/getTestRecordsPage")
    public AjaxResult getTestRecordsPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer minAccuracyRate,
            @RequestParam(required = false) Integer maxAccuracyRate) {
        try {
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<MathTestRecordDTO> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
            
            com.baomidou.mybatisplus.core.metadata.IPage<MathTestRecordDTO> result = 
                mathTestService.getTestRecordsPage(page, studentName, startDate, endDate, minAccuracyRate, maxAccuracyRate);
            
            return success(result);
        } catch (Exception e) {
            logger.error("分页查询测试记录失败", e);
            return error("分页查询测试记录失败: " + e.getMessage());
        }
    }
}

