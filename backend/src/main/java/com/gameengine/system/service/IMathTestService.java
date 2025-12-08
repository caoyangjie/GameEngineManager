package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.dto.MathTestRecordDTO;

/**
 * 数学测试服务 业务层
 * 
 * @author GameEngine
 */
public interface IMathTestService {
    
    /**
     * 生成数学测试题目
     * 
     * @param operationTypes 计算类型列表（加减乘除）
     * @param count 题目数量
     * @param minNumber 数字范围最小值
     * @param maxNumber 数字范围最大值
     * @return 测试题目列表
     */
    List<MathTestRecordDTO.MathTestQuestionDTO> generateTestQuestions(
            List<String> operationTypes,
            Integer count,
            Integer minNumber,
            Integer maxNumber);
    
    /**
     * 保存测试记录
     * 
     * @param recordDTO 测试记录DTO
     * @return 保存后的测试记录DTO
     */
    MathTestRecordDTO saveTestRecord(MathTestRecordDTO recordDTO);
    
    /**
     * 获取所有测试记录
     * 
     * @return 测试记录列表
     */
    List<MathTestRecordDTO> getAllTestRecords();
    
    /**
     * 根据ID获取测试记录
     * 
     * @param id 测试记录ID
     * @return 测试记录DTO
     */
    MathTestRecordDTO getTestRecordById(Long id);
    
    /**
     * 删除测试记录
     * 
     * @param id 测试记录ID
     * @return 是否删除成功
     */
    boolean deleteTestRecord(Long id);
    
    /**
     * 分页查询测试记录
     * 
     * @param page 分页对象
     * @param studentName 学生姓名（模糊查询）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param minAccuracyRate 最小正确率
     * @param maxAccuracyRate 最大正确率
     * @return 分页结果
     */
    IPage<MathTestRecordDTO> getTestRecordsPage(
            Page<MathTestRecordDTO> page,
            String studentName,
            String startDate,
            String endDate,
            Integer minAccuracyRate,
            Integer maxAccuracyRate);
}

