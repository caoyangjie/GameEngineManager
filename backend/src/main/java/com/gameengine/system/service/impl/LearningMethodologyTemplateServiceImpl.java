package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.utils.StringUtils;
import com.gameengine.system.domain.LearningMethodologyTemplate;
import com.gameengine.system.mapper.LearningMethodologyTemplateMapper;
import com.gameengine.system.service.ILearningMethodologyTemplateService;

/**
 * 教育学习方法论模板 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class LearningMethodologyTemplateServiceImpl implements ILearningMethodologyTemplateService {
    
    @Autowired
    private LearningMethodologyTemplateMapper templateMapper;
    
    @Override
    public IPage<LearningMethodologyTemplate> selectTemplateList(Page<LearningMethodologyTemplate> page, LearningMethodologyTemplate template) {
        LambdaQueryWrapper<LearningMethodologyTemplate> wrapper = buildQueryWrapper(template);
        return templateMapper.selectPage(page, wrapper);
    }
    
    @Override
    public List<LearningMethodologyTemplate> selectTemplateList(LearningMethodologyTemplate template) {
        LambdaQueryWrapper<LearningMethodologyTemplate> wrapper = buildQueryWrapper(template);
        return templateMapper.selectList(wrapper);
    }
    
    @Override
    public LearningMethodologyTemplate selectTemplateById(Long templateId) {
        return templateMapper.selectById(templateId);
    }
    
    @Override
    public int insertTemplate(LearningMethodologyTemplate template) {
        Date now = new Date();
        if (template.getCreateTime() == null) {
            template.setCreateTime(now);
        }
        if (template.getUpdateTime() == null) {
            template.setUpdateTime(now);
        }
        return templateMapper.insert(template);
    }
    
    @Override
    public int updateTemplate(LearningMethodologyTemplate template) {
        template.setUpdateTime(new Date());
        return templateMapper.updateById(template);
    }
    
    @Override
    public int deleteTemplateByIds(Long[] templateIds) {
        return templateMapper.deleteBatchIds(java.util.Arrays.asList(templateIds));
    }
    
    @Override
    public int deleteTemplateById(Long templateId) {
        return templateMapper.deleteById(templateId);
    }
    
    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<LearningMethodologyTemplate> buildQueryWrapper(LearningMethodologyTemplate template) {
        LambdaQueryWrapper<LearningMethodologyTemplate> wrapper = new LambdaQueryWrapper<>();
        
        if (template != null) {
            // 标题模糊查询
            if (StringUtils.isNotEmpty(template.getTitle())) {
                wrapper.like(LearningMethodologyTemplate::getTitle, template.getTitle());
            }
            
            // 状态查询
            if (StringUtils.isNotEmpty(template.getStatus())) {
                wrapper.eq(LearningMethodologyTemplate::getStatus, template.getStatus());
            }
            
            // 搜索值（用于通用搜索）
            if (StringUtils.isNotEmpty(template.getSearchValue())) {
                wrapper.and(w -> w.like(LearningMethodologyTemplate::getTitle, template.getSearchValue())
                    .or().like(LearningMethodologyTemplate::getDescription, template.getSearchValue()));
            }
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(LearningMethodologyTemplate::getCreateTime);
        
        return wrapper;
    }
}

