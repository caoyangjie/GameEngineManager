package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.utils.StringUtils;
import com.gameengine.system.domain.LearningRecord;
import com.gameengine.system.mapper.LearningRecordMapper;
import com.gameengine.system.service.ILearningRecordService;

/**
 * 学习记录 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class LearningRecordServiceImpl implements ILearningRecordService {
    
    @Autowired
    private LearningRecordMapper recordMapper;
    
    @Override
    public IPage<LearningRecord> selectRecordList(Page<LearningRecord> page, LearningRecord record, Long userId) {
        LambdaQueryWrapper<LearningRecord> wrapper = buildQueryWrapper(record, userId);
        return recordMapper.selectPage(page, wrapper);
    }
    
    @Override
    public List<LearningRecord> selectRecordList(LearningRecord record, Long userId) {
        LambdaQueryWrapper<LearningRecord> wrapper = buildQueryWrapper(record, userId);
        return recordMapper.selectList(wrapper);
    }
    
    @Override
    public LearningRecord selectRecordById(Long recordId, Long userId) {
        LambdaQueryWrapper<LearningRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningRecord::getRecordId, recordId);
        wrapper.eq(LearningRecord::getUserId, userId);
        return recordMapper.selectOne(wrapper);
    }
    
    @Override
    public int insertRecord(LearningRecord record) {
        Date now = new Date();
        if (record.getCreateTime() == null) {
            record.setCreateTime(now);
        }
        if (record.getUpdateTime() == null) {
            record.setUpdateTime(now);
        }
        return recordMapper.insert(record);
    }
    
    @Override
    public int updateRecord(LearningRecord record) {
        record.setUpdateTime(new Date());
        return recordMapper.updateById(record);
    }
    
    @Override
    public int deleteRecordByIds(Long[] recordIds, Long userId) {
        LambdaQueryWrapper<LearningRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LearningRecord::getRecordId, java.util.Arrays.asList(recordIds));
        wrapper.eq(LearningRecord::getUserId, userId);
        return recordMapper.delete(wrapper);
    }
    
    @Override
    public int deleteRecordById(Long recordId, Long userId) {
        LambdaQueryWrapper<LearningRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningRecord::getRecordId, recordId);
        wrapper.eq(LearningRecord::getUserId, userId);
        return recordMapper.delete(wrapper);
    }
    
    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<LearningRecord> buildQueryWrapper(LearningRecord record, Long userId) {
        LambdaQueryWrapper<LearningRecord> wrapper = new LambdaQueryWrapper<>();
        
        // 必须按用户ID过滤
        wrapper.eq(LearningRecord::getUserId, userId);
        
        if (record != null) {
            // 模板ID查询
            if (record.getTemplateId() != null) {
                wrapper.eq(LearningRecord::getTemplateId, record.getTemplateId());
            }
            
            // 学生姓名模糊查询
            if (StringUtils.isNotEmpty(record.getStudentName())) {
                wrapper.like(LearningRecord::getStudentName, record.getStudentName());
            }
            
            // 学习主题模糊查询
            if (StringUtils.isNotEmpty(record.getLearningTopic())) {
                wrapper.like(LearningRecord::getLearningTopic, record.getLearningTopic());
            }
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(LearningRecord::getCreateTime);
        
        return wrapper;
    }
}

