package com.gameengine.system.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.utils.StringUtils;
import com.gameengine.system.domain.MathTestRecord;
import com.gameengine.system.domain.dto.MathTestRecordDTO;
import com.gameengine.system.mapper.MathTestRecordMapper;
import com.gameengine.system.service.IMathTestService;

/**
 * 数学测试服务 业务层实现
 * 
 * @author GameEngine
 */
@Service
public class MathTestServiceImpl implements IMathTestService {
    
    @Autowired
    private MathTestRecordMapper testRecordMapper;
    
    private static final Random random = new Random();
    
    @Override
    public List<MathTestRecordDTO.MathTestQuestionDTO> generateTestQuestions(
            List<String> operationTypes,
            Integer count,
            Integer minNumber,
            Integer maxNumber) {
        
        if (operationTypes == null || operationTypes.isEmpty()) {
            throw new RuntimeException("至少需要选择一种计算类型");
        }
        
        if (count == null || count <= 0) {
            throw new RuntimeException("题目数量必须大于0");
        }
        
        if (minNumber == null || maxNumber == null || minNumber >= maxNumber) {
            throw new RuntimeException("数字范围无效");
        }
        
        List<MathTestRecordDTO.MathTestQuestionDTO> questions = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            // 随机选择一种计算类型
            String operator = operationTypes.get(random.nextInt(operationTypes.size()));
            
            MathTestRecordDTO.MathTestQuestionDTO question = new MathTestRecordDTO.MathTestQuestionDTO();
            
            int num1, num2, correctAnswer;
            
            switch (operator) {
                case "+":
                    // 加法：确保结果不超过maxNumber
                    num1 = random.nextInt(maxNumber - minNumber + 1) + minNumber;
                    int maxNum2 = Math.min(maxNumber - num1, maxNumber - minNumber);
                    if (maxNum2 < 0) maxNum2 = 0;
                    num2 = random.nextInt(maxNum2 + 1) + minNumber;
                    correctAnswer = num1 + num2;
                    break;
                    
                case "-":
                    // 减法：确保结果不为负数
                    num1 = random.nextInt(maxNumber - minNumber + 1) + minNumber;
                    num2 = random.nextInt(num1 - minNumber + 1) + minNumber;
                    correctAnswer = num1 - num2;
                    break;
                    
                case "*":
                    // 乘法：限制乘数范围，避免结果过大
                    int maxMultiplier = Math.min(20, maxNumber);
                    num1 = random.nextInt(maxMultiplier - minNumber + 1) + minNumber;
                    num2 = random.nextInt(maxMultiplier - minNumber + 1) + minNumber;
                    correctAnswer = num1 * num2;
                    break;
                    
                case "/":
                    // 除法：确保能整除
                    num2 = random.nextInt(maxNumber - minNumber + 1) + minNumber;
                    if (num2 == 0) num2 = 1; // 避免除零
                    int quotient = random.nextInt((maxNumber - minNumber) / num2 + 1) + 1;
                    num1 = num2 * quotient;
                    correctAnswer = quotient;
                    break;
                    
                default:
                    throw new RuntimeException("不支持的计算类型: " + operator);
            }
            
            question.setNum1(num1);
            question.setNum2(num2);
            question.setOperator(operator);
            question.setCorrectAnswer(correctAnswer);
            question.setStudentAnswer(null);
            question.setIsCorrect(null);
            
            questions.add(question);
        }
        
        // 打乱题目顺序
        Collections.shuffle(questions, random);
        
        return questions;
    }
    
    @Override
    public MathTestRecordDTO saveTestRecord(MathTestRecordDTO recordDTO) {
        MathTestRecord record = new MathTestRecord();
        
        // 如果ID存在，则为更新操作
        if (recordDTO.getId() != null) {
            record = testRecordMapper.selectById(recordDTO.getId());
            if (record == null) {
                throw new RuntimeException("测试记录不存在");
            }
        } else {
            record.setCreateTime(new Date());
        }
        
        // 设置字段
        record.setStudentName(recordDTO.getStudentName());
        record.setTestDate(recordDTO.getTestDate());
        record.setTestCount(recordDTO.getTestCount());
        record.setMinNumber(recordDTO.getMinNumber());
        record.setMaxNumber(recordDTO.getMaxNumber());
        record.setCorrectCount(recordDTO.getCorrectCount());
        record.setIncorrectCount(recordDTO.getIncorrectCount());
        record.setAccuracyRate(recordDTO.getAccuracyRate());
        record.setUpdateTime(new Date());
        
        // 将操作类型列表转换为JSON字符串
        if (recordDTO.getOperationTypes() != null) {
            record.setOperationTypes(JSON.toJSONString(recordDTO.getOperationTypes()));
        }
        
        // 将题目列表转换为JSON字符串
        if (recordDTO.getQuestions() != null) {
            record.setQuestions(JSON.toJSONString(recordDTO.getQuestions()));
        }
        
        // 保存或更新
        if (record.getId() == null) {
            testRecordMapper.insert(record);
        } else {
            testRecordMapper.updateById(record);
        }
        
        // 转换为DTO返回
        return convertToDTO(record);
    }
    
    @Override
    public List<MathTestRecordDTO> getAllTestRecords() {
        QueryWrapper<MathTestRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<MathTestRecord> records = testRecordMapper.selectList(queryWrapper);
        
        return records.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public MathTestRecordDTO getTestRecordById(Long id) {
        MathTestRecord record = testRecordMapper.selectById(id);
        if (record == null) {
            return null;
        }
        return convertToDTO(record);
    }
    
    @Override
    public boolean deleteTestRecord(Long id) {
        int result = testRecordMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    public IPage<MathTestRecordDTO> getTestRecordsPage(
            Page<MathTestRecordDTO> page,
            String studentName,
            String startDate,
            String endDate,
            Integer minAccuracyRate,
            Integer maxAccuracyRate) {
        
        // 构建查询条件
        QueryWrapper<MathTestRecord> queryWrapper = buildQueryWrapper(
                studentName, startDate, endDate, minAccuracyRate, maxAccuracyRate);
        
        // 执行分页查询（使用实体类的Page对象）
        Page<MathTestRecord> entityPage = new Page<>(page.getCurrent(), page.getSize());
        IPage<MathTestRecord> entityResult = testRecordMapper.selectPage(entityPage, queryWrapper);
        
        // 转换为DTO分页结果
        Page<MathTestRecordDTO> dtoPage = new Page<>(entityResult.getCurrent(), entityResult.getSize(), entityResult.getTotal());
        List<MathTestRecordDTO> dtoList = entityResult.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        
        return dtoPage;
    }
    
    /**
     * 构建查询条件
     */
    private QueryWrapper<MathTestRecord> buildQueryWrapper(
            String studentName,
            String startDate,
            String endDate,
            Integer minAccuracyRate,
            Integer maxAccuracyRate) {
        
        QueryWrapper<MathTestRecord> queryWrapper = new QueryWrapper<>();
        
        // 学生姓名模糊查询
        if (StringUtils.isNotEmpty(studentName)) {
            queryWrapper.like("student_name", studentName);
        }
        
        // 日期范围查询
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotEmpty(startDate)) {
            try {
                Date start = dateFormat.parse(startDate);
                queryWrapper.ge("test_date", start);
            } catch (ParseException e) {
                // 日期格式错误，忽略该条件
            }
        }
        
        if (StringUtils.isNotEmpty(endDate)) {
            try {
                Date end = dateFormat.parse(endDate);
                // 结束日期需要包含当天，所以加一天，使用lt确保包含当天
                Date endDateInclusive = new Date(end.getTime() + 24 * 60 * 60 * 1000L);
                queryWrapper.lt("test_date", endDateInclusive);
            } catch (ParseException e) {
                // 日期格式错误，忽略该条件
            }
        }
        
        // 正确率范围查询
        if (minAccuracyRate != null) {
            queryWrapper.ge("accuracy_rate", minAccuracyRate);
        }
        
        if (maxAccuracyRate != null) {
            queryWrapper.le("accuracy_rate", maxAccuracyRate);
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");
        
        return queryWrapper;
    }
    
    /**
     * 将实体转换为DTO
     */
    private MathTestRecordDTO convertToDTO(MathTestRecord record) {
        MathTestRecordDTO dto = new MathTestRecordDTO();
        dto.setId(record.getId());
        dto.setStudentName(record.getStudentName());
        dto.setTestDate(record.getTestDate());
        dto.setTestCount(record.getTestCount());
        dto.setMinNumber(record.getMinNumber());
        dto.setMaxNumber(record.getMaxNumber());
        dto.setCorrectCount(record.getCorrectCount());
        dto.setIncorrectCount(record.getIncorrectCount());
        dto.setAccuracyRate(record.getAccuracyRate());
        dto.setCreateTime(record.getCreateTime());
        dto.setUpdateTime(record.getUpdateTime());
        
        // 将JSON字符串转换为操作类型列表
        if (record.getOperationTypes() != null && !record.getOperationTypes().isEmpty()) {
            try {
                List<String> operationTypes = JSON.parseArray(record.getOperationTypes(), String.class);
                dto.setOperationTypes(operationTypes);
            } catch (Exception e) {
                dto.setOperationTypes(new ArrayList<>());
            }
        } else {
            dto.setOperationTypes(new ArrayList<>());
        }
        
        // 将JSON字符串转换为题目列表
        if (record.getQuestions() != null && !record.getQuestions().isEmpty()) {
            try {
                List<MathTestRecordDTO.MathTestQuestionDTO> questions = 
                    JSON.parseArray(record.getQuestions(), MathTestRecordDTO.MathTestQuestionDTO.class);
                dto.setQuestions(questions);
            } catch (Exception e) {
                dto.setQuestions(new ArrayList<>());
            }
        } else {
            dto.setQuestions(new ArrayList<>());
        }
        
        return dto;
    }
}

