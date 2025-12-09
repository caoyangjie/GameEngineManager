package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameengine.system.domain.AttentionSudokuRecord;
import com.gameengine.system.mapper.AttentionSudokuRecordMapper;
import com.gameengine.system.service.IAttentionSudokuService;

/**
 * 数独游戏服务 业务层实现
 * 
 * @author GameEngine
 */
@Service
public class AttentionSudokuServiceImpl implements IAttentionSudokuService {
    
    @Autowired
    private AttentionSudokuRecordMapper recordMapper;
    
    @Override
    public AttentionSudokuRecord saveRecord(AttentionSudokuRecord record) {
        Date now = new Date();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        recordMapper.insert(record);
        return record;
    }
    
    @Override
    public List<AttentionSudokuRecord> getTopRecords(Integer gridSize, String difficulty, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        
        QueryWrapper<AttentionSudokuRecord> queryWrapper = new QueryWrapper<>();
        
        // 根据gridSize筛选
        if (gridSize != null && gridSize > 0) {
            queryWrapper.eq("grid_size", gridSize);
        }
        
        // 根据difficulty筛选
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            queryWrapper.eq("difficulty", difficulty);
        }
        
        // 按用时升序排列
        queryWrapper.orderByAsc("duration_ms");
        queryWrapper.last("LIMIT " + limit);
        
        return recordMapper.selectList(queryWrapper);
    }
}

