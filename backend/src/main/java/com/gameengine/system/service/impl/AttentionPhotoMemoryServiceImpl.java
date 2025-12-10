package com.gameengine.system.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameengine.system.domain.AttentionPhotoMemoryRecord;
import com.gameengine.system.mapper.AttentionPhotoMemoryRecordMapper;
import com.gameengine.system.service.IAttentionPhotoMemoryService;

/**
 * 照相记忆游戏服务 业务层实现
 * 
 * @author GameEngine
 */
@Service
public class AttentionPhotoMemoryServiceImpl implements IAttentionPhotoMemoryService {
    
    @Autowired
    private AttentionPhotoMemoryRecordMapper recordMapper;
    
    @Override
    public AttentionPhotoMemoryRecord saveRecord(AttentionPhotoMemoryRecord record) {
        Date now = new Date();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        
        // 确保准确率在0-100范围内
        if (record.getAccuracy() != null) {
            BigDecimal accuracy = record.getAccuracy();
            if (accuracy.compareTo(BigDecimal.ZERO) < 0) {
                accuracy = BigDecimal.ZERO;
            } else if (accuracy.compareTo(new BigDecimal("100")) > 0) {
                accuracy = new BigDecimal("100");
            }
            record.setAccuracy(accuracy);
        }
        
        recordMapper.insert(record);
        return record;
    }
    
    @Override
    public List<AttentionPhotoMemoryRecord> getTopRecords(Integer gridSize, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        
        QueryWrapper<AttentionPhotoMemoryRecord> queryWrapper = new QueryWrapper<>();
        
        // 根据gridSize筛选
        if (gridSize != null && gridSize > 0) {
            queryWrapper.eq("grid_size", gridSize);
        }
        
        // 按准确率降序排列
        queryWrapper.orderByDesc("accuracy");
        queryWrapper.last("LIMIT " + limit);
        
        return recordMapper.selectList(queryWrapper);
    }
}

