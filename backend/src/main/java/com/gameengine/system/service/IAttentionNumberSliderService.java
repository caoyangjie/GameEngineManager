package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.AttentionNumberSliderRecord;

/**
 * 数字华容道游戏服务 业务层
 * 
 * @author GameEngine
 */
public interface IAttentionNumberSliderService {
    
    /**
     * 保存游戏记录
     * 
     * @param record 游戏记录
     * @return 保存后的记录
     */
    AttentionNumberSliderRecord saveRecord(AttentionNumberSliderRecord record);
    
    /**
     * 获取TOP10最快记录
     * 
     * @param gridSize 方格尺寸，用于区分不同尺寸的排行榜
     * @param limit 返回记录数，默认10
     * @return TOP10记录列表，按用时升序
     */
    List<AttentionNumberSliderRecord> getTopRecords(Integer gridSize, Integer limit);
}

