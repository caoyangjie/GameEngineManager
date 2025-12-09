package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.AttentionSudokuRecord;

/**
 * 数独游戏服务 业务层
 * 
 * @author GameEngine
 */
public interface IAttentionSudokuService {
    
    /**
     * 保存游戏记录
     * 
     * @param record 游戏记录
     * @return 保存后的记录
     */
    AttentionSudokuRecord saveRecord(AttentionSudokuRecord record);
    
    /**
     * 获取TOP10最快记录
     * 
     * @param gridSize 数独尺寸，用于区分不同尺寸的排行榜
     * @param difficulty 难度级别，用于区分不同难度的排行榜
     * @param limit 返回记录数，默认10
     * @return TOP10记录列表，按用时升序
     */
    List<AttentionSudokuRecord> getTopRecords(Integer gridSize, String difficulty, Integer limit);
}

