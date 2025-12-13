package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameengine.system.domain.AttentionNumberFaxRecord;
import com.gameengine.system.mapper.AttentionNumberFaxRecordMapper;
import com.gameengine.system.service.IAttentionNumberFaxService;

/**
 * 数字传真训练服务 业务层实现
 * 
 * @author GameEngine
 */
@Service
public class AttentionNumberFaxServiceImpl implements IAttentionNumberFaxService {
    
    @Autowired
    private AttentionNumberFaxRecordMapper recordMapper;
    
    @Override
    public AttentionNumberFaxRecord saveRecord(AttentionNumberFaxRecord record) {
        Date now = new Date();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        recordMapper.insert(record);
        return record;
    }
    
    @Override
    public List<AttentionNumberFaxRecord> getRecords(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        
        QueryWrapper<AttentionNumberFaxRecord> queryWrapper = new QueryWrapper<>();
        
        // 按创建时间降序排列
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT " + limit);
        
        return recordMapper.selectList(queryWrapper);
    }
}

