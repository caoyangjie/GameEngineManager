package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameengine.system.domain.PegMemoryRecord;
import com.gameengine.system.domain.PegMemoryTemplate;
import com.gameengine.system.mapper.PegMemoryRecordMapper;
import com.gameengine.system.mapper.PegMemoryTemplateMapper;
import com.gameengine.system.service.IPegMemoryService;

/**
 * 定桩记忆法服务实现
 */
@Service
public class PegMemoryServiceImpl implements IPegMemoryService {
    
    private static final Logger log = LoggerFactory.getLogger(PegMemoryServiceImpl.class);
    
    @Autowired
    private PegMemoryRecordMapper recordMapper;
    
    @Autowired
    private PegMemoryTemplateMapper templateMapper;
    
    @Override
    public PegMemoryRecord saveRecord(PegMemoryRecord record) {
        Date now = new Date();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        
        if (record.getCorrectCount() == null) {
            record.setCorrectCount(0);
        }
        if (record.getTotalCount() == null) {
            record.setTotalCount(0);
        }
        if (record.getDurationSeconds() == null) {
            record.setDurationSeconds(0);
        }
        if (record.getAccuracy() == null) {
            int total = record.getTotalCount() == null ? 0 : record.getTotalCount();
            int correct = record.getCorrectCount() == null ? 0 : record.getCorrectCount();
            int accuracy = total > 0 ? (int) Math.round(correct * 100.0 / total) : 0;
            record.setAccuracy(accuracy);
        }
        
        recordMapper.insert(record);
        return record;
    }
    
    @Override
    public List<PegMemoryRecord> getTopRecords(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        QueryWrapper<PegMemoryRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("accuracy").orderByDesc("correct_count").last("LIMIT " + limit);
        return recordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<PegMemoryTemplate> getAllTemplates() {
        QueryWrapper<PegMemoryTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("is_default").orderByDesc("create_time");
        return templateMapper.selectList(queryWrapper);
    }
    
    @Override
    public PegMemoryTemplate getTemplateById(Long id) {
        if (id == null) {
            return null;
        }
        return templateMapper.selectById(id);
    }
    
    @Override
    public PegMemoryTemplate saveTemplate(PegMemoryTemplate template, Long userId) {
        Date now = new Date();
        template.setUpdateTime(now);
        template.setCreatedBy(userId);
        
        // 计算定桩总数
        if (StringUtils.hasText(template.getPegItems())) {
            try {
                JSONArray items = JSON.parseArray(template.getPegItems());
                if (items != null) {
                    template.setTotalPegs(items.size());
                } else {
                    template.setTotalPegs(0);
                }
            } catch (Exception e) {
                log.warn("解析定桩项列表失败", e);
                template.setTotalPegs(0);
            }
        } else {
            template.setTotalPegs(0);
        }
        
        if (template.getId() == null) {
            // 新增
            template.setCreateTime(now);
            templateMapper.insert(template);
        } else {
            // 更新
            templateMapper.updateById(template);
        }
        
        return template;
    }
    
    @Override
    public boolean deleteTemplate(Long id, Long userId) {
        if (id == null) {
            return false;
        }
        PegMemoryTemplate template = templateMapper.selectById(id);
        if (template == null) {
            return false;
        }
        // 检查权限：只有创建者可以删除
        if (userId != null && template.getCreatedBy() != null && !template.getCreatedBy().equals(userId)) {
            log.warn("用户 {} 尝试删除非自己创建的模板 {}", userId, id);
            return false;
        }
        templateMapper.deleteById(id);
        return true;
    }
    
    @Override
    public List<PegMemoryTemplate> getDefaultTemplates() {
        QueryWrapper<PegMemoryTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_default", 1).orderByDesc("create_time");
        return templateMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<PegMemoryTemplate> getTemplatesByCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return getAllTemplates();
        }
        QueryWrapper<PegMemoryTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category).orderByDesc("is_default").orderByDesc("create_time");
        return templateMapper.selectList(queryWrapper);
    }
}

