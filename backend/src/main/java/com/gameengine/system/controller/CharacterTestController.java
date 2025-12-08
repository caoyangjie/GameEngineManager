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
    
    /**
     * 获取测试汉字
     * 
     * @param educationLevel 教育阶段: primary(小学), middle(初中), high(高中)
     * @param count 测试字数
     * @return 测试汉字列表
     */
    @Operation(summary = "获取测试汉字", description = "根据教育阶段和数量获取测试汉字")
    @GetMapping("/getCharacters")
    public AjaxResult getCharacters(
            @RequestParam(required = false, defaultValue = "primary") String educationLevel,
            @RequestParam(required = false, defaultValue = "50") Integer count) {
        try {
            List<CharacterTestDTO> characters = characterTestService.getTestCharacters(educationLevel, count);
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
    public AjaxResult saveTestRecord(@RequestBody CharacterTestRecordDTO recordDTO) {
        try {
            CharacterTestRecordDTO savedRecord = characterTestService.saveTestRecord(recordDTO);
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
            List<CharacterTestRecordDTO> records = characterTestService.getAllTestRecords();
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
            CharacterTestRecordDTO record = characterTestService.getTestRecordById(id);
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
            boolean success = characterTestService.deleteTestRecord(id);
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
}

