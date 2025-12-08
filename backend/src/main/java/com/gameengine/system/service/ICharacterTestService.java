package com.gameengine.system.service;

import java.util.List;

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
     * @param count 测试字数
     * @return 测试汉字列表
     */
    List<CharacterTestDTO> getTestCharacters(String educationLevel, Integer count);
    
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
     * @return 保存后的测试记录DTO
     */
    CharacterTestRecordDTO saveTestRecord(CharacterTestRecordDTO recordDTO);
    
    /**
     * 获取所有测试记录
     * 
     * @return 测试记录列表
     */
    List<CharacterTestRecordDTO> getAllTestRecords();
    
    /**
     * 根据ID获取测试记录
     * 
     * @param id 测试记录ID
     * @return 测试记录DTO
     */
    CharacterTestRecordDTO getTestRecordById(Long id);
    
    /**
     * 删除测试记录
     * 
     * @param id 测试记录ID
     * @return 是否删除成功
     */
    boolean deleteTestRecord(Long id);
}

