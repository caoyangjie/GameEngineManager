package com.gameengine.system.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.utils.StringUtils;
import com.gameengine.system.domain.CharacterTestRecord;
import com.gameengine.system.domain.dto.CharacterTestDTO;
import com.gameengine.system.domain.dto.CharacterTestRecordDTO;
import com.gameengine.system.mapper.CharacterTestRecordMapper;
import com.gameengine.system.service.ICharacterTestService;

/**
 * 识字测试服务 业务层实现
 * 
 * @author GameEngine
 */
@Service
public class CharacterTestServiceImpl implements ICharacterTestService {
    
    @Autowired
    private CharacterTestRecordMapper testRecordMapper;
    
    private static List<CharacterTestDTO> allCharacters = null;
    private static final Random random = new Random();
    
    /**
     * 加载汉字数据
     */
    private synchronized void loadCharacters() {
        if (allCharacters != null) {
            return;
        }
        
        try {
            ClassPathResource resource = new ClassPathResource("data/chinese_characters.json");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            String jsonContent = sb.toString();
            
            JSONArray jsonArray = JSON.parseArray(jsonContent);
            allCharacters = new ArrayList<>();
            
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                CharacterTestDTO dto = new CharacterTestDTO();
                dto.setCharacter(obj.getString("character"));
                dto.setPinyin(obj.getString("pinyin"));
                dto.setEducationLevel(obj.getString("educationLevel"));
                dto.setGrade(obj.getString("grade"));
                allCharacters.add(dto);
            }
        } catch (Exception e) {
            // 如果加载失败，使用默认数据
            allCharacters = getDefaultCharacters();
        }
    }
    
    /**
     * 获取默认汉字数据（当JSON文件加载失败时使用）
     */
    private List<CharacterTestDTO> getDefaultCharacters() {
        List<CharacterTestDTO> defaultChars = new ArrayList<>();
        String[] primaryChars = {"爸", "妈", "我", "大", "米", "土", "地", "马", "花", "哥"};
        String[] primaryPinyins = {"bà", "mā", "wǒ", "dà", "mǐ", "tǔ", "dì", "mǎ", "huā", "gē"};
        
        for (int i = 0; i < primaryChars.length; i++) {
            CharacterTestDTO dto = new CharacterTestDTO();
            dto.setCharacter(primaryChars[i]);
            dto.setPinyin(primaryPinyins[i]);
            dto.setEducationLevel("primary");
            dto.setGrade("primary-1");
            defaultChars.add(dto);
        }
        return defaultChars;
    }
    
    @Override
    public List<CharacterTestDTO> getTestCharacters(String educationLevel, String grade, Integer count) {
        loadCharacters();
        
        if (allCharacters == null || allCharacters.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 根据教育阶段筛选汉字
        List<CharacterTestDTO> filteredChars = allCharacters.stream()
            .filter(charDto -> {
                if (StringUtils.isNotEmpty(grade)) {
                    return grade.equals(charDto.getGrade());
                }
                if (StringUtils.isEmpty(educationLevel)) {
                    return true;
                }
                return educationLevel.equals(charDto.getEducationLevel());
            })
            .collect(Collectors.toList());
        
        // 如果筛选后的字符数量不足，使用所有字符
        if (filteredChars.isEmpty()) {
            filteredChars = new ArrayList<>(allCharacters);
        }
        
        // 随机选择指定数量的汉字
        List<CharacterTestDTO> result = new ArrayList<>();
        if (count != null && count > 0) {
            int maxCount = Math.min(count, filteredChars.size());
            Collections.shuffle(filteredChars, random);
            result = filteredChars.subList(0, maxCount);
        } else {
            result = filteredChars;
        }
        
        return result;
    }
    
    @Override
    public List<CharacterTestDTO> getAllCharacters() {
        loadCharacters();
        return allCharacters != null ? new ArrayList<>(allCharacters) : new ArrayList<>();
    }
    
    @Override
    public CharacterTestRecordDTO saveTestRecord(CharacterTestRecordDTO recordDTO) {
        CharacterTestRecord record = new CharacterTestRecord();
        
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
        record.setEducationLevel(recordDTO.getEducationLevel());
        record.setTestCount(recordDTO.getTestCount());
        record.setShowPinyin(recordDTO.getShowPinyin());
        record.setCorrectCount(recordDTO.getCorrectCount());
        record.setIncorrectCount(recordDTO.getIncorrectCount());
        record.setMasteredCount(recordDTO.getMasteredCount());
        record.setMasteryRate(recordDTO.getMasteryRate());
        record.setUpdateTime(new Date());
        
        // 将字符列表转换为JSON字符串
        if (recordDTO.getCharacters() != null) {
            record.setCharacters(JSON.toJSONString(recordDTO.getCharacters()));
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
    public List<CharacterTestRecordDTO> getAllTestRecords() {
        QueryWrapper<CharacterTestRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<CharacterTestRecord> records = testRecordMapper.selectList(queryWrapper);
        
        return records.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public CharacterTestRecordDTO getTestRecordById(Long id) {
        CharacterTestRecord record = testRecordMapper.selectById(id);
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
    public IPage<CharacterTestRecordDTO> getTestRecordsPage(
            Page<CharacterTestRecordDTO> page,
            String studentName,
            String startDate,
            String endDate,
            Integer minMasteryRate,
            Integer maxMasteryRate) {
        
        // 构建查询条件
        QueryWrapper<CharacterTestRecord> queryWrapper = buildQueryWrapper(
                studentName, startDate, endDate, minMasteryRate, maxMasteryRate);
        
        // 执行分页查询（使用实体类的Page对象）
        Page<CharacterTestRecord> entityPage = new Page<>(page.getCurrent(), page.getSize());
        IPage<CharacterTestRecord> entityResult = testRecordMapper.selectPage(entityPage, queryWrapper);
        
        // 转换为DTO分页结果
        Page<CharacterTestRecordDTO> dtoPage = new Page<>(entityResult.getCurrent(), entityResult.getSize(), entityResult.getTotal());
        List<CharacterTestRecordDTO> dtoList = entityResult.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        
        return dtoPage;
    }
    
    /**
     * 构建查询条件
     */
    private QueryWrapper<CharacterTestRecord> buildQueryWrapper(
            String studentName,
            String startDate,
            String endDate,
            Integer minMasteryRate,
            Integer maxMasteryRate) {
        
        QueryWrapper<CharacterTestRecord> queryWrapper = new QueryWrapper<>();
        
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
        
        // 掌握率范围查询
        if (minMasteryRate != null) {
            queryWrapper.ge("mastery_rate", minMasteryRate);
        }
        
        if (maxMasteryRate != null) {
            queryWrapper.le("mastery_rate", maxMasteryRate);
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");
        
        return queryWrapper;
    }
    
    /**
     * 将实体转换为DTO
     */
    private CharacterTestRecordDTO convertToDTO(CharacterTestRecord record) {
        CharacterTestRecordDTO dto = new CharacterTestRecordDTO();
        dto.setId(record.getId());
        dto.setStudentName(record.getStudentName());
        dto.setTestDate(record.getTestDate());
        dto.setEducationLevel(record.getEducationLevel());
        dto.setTestCount(record.getTestCount());
        dto.setShowPinyin(record.getShowPinyin());
        dto.setCorrectCount(record.getCorrectCount());
        dto.setIncorrectCount(record.getIncorrectCount());
        dto.setMasteredCount(record.getMasteredCount());
        dto.setMasteryRate(record.getMasteryRate());
        dto.setCreateTime(record.getCreateTime());
        dto.setUpdateTime(record.getUpdateTime());
        
        // 将JSON字符串转换为字符列表
        if (record.getCharacters() != null && !record.getCharacters().isEmpty()) {
            try {
                List<CharacterTestRecordDTO.CharacterTestItemDTO> characters = 
                    JSON.parseArray(record.getCharacters(), CharacterTestRecordDTO.CharacterTestItemDTO.class);
                dto.setCharacters(characters);
            } catch (Exception e) {
                // 如果解析失败，设置为空列表
                dto.setCharacters(new ArrayList<>());
            }
        } else {
            dto.setCharacters(new ArrayList<>());
        }
        
        return dto;
    }
}

