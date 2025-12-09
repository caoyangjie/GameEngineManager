package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameengine.system.domain.AttentionDigitIntroRecord;
import com.gameengine.system.mapper.AttentionDigitIntroRecordMapper;
import com.gameengine.system.service.IAttentionDigitIntroService;

/**
 * 数字入门游戏服务 业务层实现
 * 
 * @author GameEngine
 */
@Service
public class AttentionDigitIntroServiceImpl implements IAttentionDigitIntroService {
    
    @Autowired
    private AttentionDigitIntroRecordMapper recordMapper;
    
    @Override
    public AttentionDigitIntroRecord saveRecord(AttentionDigitIntroRecord record) {
        Date now = new Date();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        recordMapper.insert(record);
        return record;
    }
    
    @Override
    public List<AttentionDigitIntroRecord> getTopRecords(Integer gridSize, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        
        QueryWrapper<AttentionDigitIntroRecord> queryWrapper = new QueryWrapper<>();
        
        // 根据gridSize筛选
        if (gridSize != null && gridSize > 0) {
            queryWrapper.eq("grid_size", gridSize);
        }
        
        // 按用时升序排列
        queryWrapper.orderByAsc("duration_ms");
        queryWrapper.last("LIMIT " + limit);
        
        return recordMapper.selectList(queryWrapper);
    }
}

