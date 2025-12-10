package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.AttentionPhotoMemoryRecord;

/**
 * 照相记忆游戏服务 业务层
 * 
 * @author GameEngine
 */
public interface IAttentionPhotoMemoryService {
    
    /**
     * 保存游戏记录
     * 
     * @param record 游戏记录
     * @return 保存后的记录
     */
    AttentionPhotoMemoryRecord saveRecord(AttentionPhotoMemoryRecord record);
    
    /**
     * 获取TOP10最高准确率记录
     * 
     * @param gridSize 方格尺寸，用于区分不同尺寸的排行榜
     * @param limit 返回记录数，默认10
     * @return TOP10记录列表，按准确率降序
     */
    List<AttentionPhotoMemoryRecord> getTopRecords(Integer gridSize, Integer limit);
}

