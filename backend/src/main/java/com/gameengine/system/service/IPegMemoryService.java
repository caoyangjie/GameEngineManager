package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.PegMemoryRecord;
import com.gameengine.system.domain.PegMemoryTemplate;

public interface IPegMemoryService {
    
    /**
     * 保存训练记录
     */
    PegMemoryRecord saveRecord(PegMemoryRecord record);
    
    /**
     * 获取排行榜
     */
    List<PegMemoryRecord> getTopRecords(Integer limit);
    
    /**
     * 获取所有模板
     */
    List<PegMemoryTemplate> getAllTemplates();
    
    /**
     * 根据ID获取模板
     */
    PegMemoryTemplate getTemplateById(Long id);
    
    /**
     * 创建或更新模板
     */
    PegMemoryTemplate saveTemplate(PegMemoryTemplate template, Long userId);
    
    /**
     * 删除模板
     */
    boolean deleteTemplate(Long id, Long userId);
    
    /**
     * 获取默认模板
     */
    List<PegMemoryTemplate> getDefaultTemplates();
    
    /**
     * 按分类获取模板
     */
    List<PegMemoryTemplate> getTemplatesByCategory(String category);
}

