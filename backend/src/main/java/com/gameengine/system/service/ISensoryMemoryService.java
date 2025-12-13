package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.AttentionSensoryMemoryContent;
import com.gameengine.system.domain.AttentionSensoryMemoryRecord;
import com.gameengine.system.domain.dto.SensoryMemoryGenerateRequest;

public interface ISensoryMemoryService {
    
    /**
     * 保存训练记录
     */
    AttentionSensoryMemoryRecord saveRecord(AttentionSensoryMemoryRecord record);
    
    /**
     * 获取排行榜
     */
    List<AttentionSensoryMemoryRecord> getTopRecords(Integer limit);
    
    /**
     * 生成感官记忆内容（调用DeepSeek生成）
     */
    List<AttentionSensoryMemoryContent> generateContents(SensoryMemoryGenerateRequest request, Long userId) throws Exception;
    
    /**
     * 获取最新内容
     */
    List<AttentionSensoryMemoryContent> getLatestContents(Integer limit);
    
    /**
     * 按标签查询内容
     */
    List<AttentionSensoryMemoryContent> getContentsByTag(String tag, Integer limit);
    
    /**
     * 获取所有标签（去重）
     */
    List<String> getAllTags();
    
    /**
     * 分页查询记录（按用户ID隔离）
     */
    List<AttentionSensoryMemoryRecord> getRecordsByUserId(Long userId, Integer pageNum, Integer pageSize);
}

