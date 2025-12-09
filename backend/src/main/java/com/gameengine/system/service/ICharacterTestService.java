package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.dto.CharacterTestDTO;
import com.gameengine.system.domain.dto.CharacterTestRecordDTO;

/**
 * 识字测试服务 业务层
 * 
 * @author GameEngine
 */
public interface ICharacterTestService {
    
    /**
     * 根据教育阶段和数量获取测试汉字
     * 
     * @param educationLevel 教育阶段: primary(小学), middle(初中), high(高中)
     * @param grade 年级（可选，例如 primary-1 表示小学一年级）
     * @param count 测试字数
     * @return 测试汉字列表
     */
    List<CharacterTestDTO> getTestCharacters(String educationLevel, String grade, Integer count);
    
    /**
     * 获取所有汉字
     * 
     * @return 所有汉字列表
     */
    List<CharacterTestDTO> getAllCharacters();
    
    /**
     * 保存测试记录
     * 
     * @param recordDTO 测试记录DTO
     * @param userId 当前登录用户ID
     * @return 保存后的测试记录DTO
     */
    CharacterTestRecordDTO saveTestRecord(CharacterTestRecordDTO recordDTO, Long userId);
    
    /**
     * 获取所有测试记录
     * 
     * @param userId 当前登录用户ID
     * @return 测试记录列表
     */
    List<CharacterTestRecordDTO> getAllTestRecords(Long userId);
    
    /**
     * 根据ID获取测试记录
     * 
     * @param id 测试记录ID
     * @param userId 当前登录用户ID
     * @return 测试记录DTO
     */
    CharacterTestRecordDTO getTestRecordById(Long id, Long userId);
    
    /**
     * 删除测试记录
     * 
     * @param id 测试记录ID
     * @param userId 当前登录用户ID
     * @return 是否删除成功
     */
    boolean deleteTestRecord(Long id, Long userId);
    
    /**
     * 分页查询测试记录
     * 
     * @param page 分页对象
     * @param studentName 学生姓名（模糊查询）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param minMasteryRate 最小掌握率
     * @param maxMasteryRate 最大掌握率
     * @param userId 当前登录用户ID
     * @return 分页结果
     */
    IPage<CharacterTestRecordDTO> getTestRecordsPage(
            Page<CharacterTestRecordDTO> page,
            String studentName,
            String startDate,
            String endDate,
            Integer minMasteryRate,
            Integer maxMasteryRate,
            Long userId);
}

