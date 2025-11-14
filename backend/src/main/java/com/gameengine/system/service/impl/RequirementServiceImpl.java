package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.utils.StringUtils;
import com.gameengine.system.domain.Requirement;
import com.gameengine.system.mapper.RequirementMapper;
import com.gameengine.system.service.IRequirementService;

/**
 * 用户需求 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class RequirementServiceImpl implements IRequirementService {
    
    @Autowired
    private RequirementMapper requirementMapper;
    
    @Override
    public IPage<Requirement> selectRequirementList(Page<Requirement> page, Requirement requirement) {
        LambdaQueryWrapper<Requirement> wrapper = buildQueryWrapper(requirement);
        return requirementMapper.selectPage(page, wrapper);
    }
    
    @Override
    public List<Requirement> selectRequirementList(Requirement requirement) {
        LambdaQueryWrapper<Requirement> wrapper = buildQueryWrapper(requirement);
        return requirementMapper.selectList(wrapper);
    }
    
    @Override
    public Requirement selectRequirementById(Long requirementId) {
        return requirementMapper.selectById(requirementId);
    }
    
    @Override
    public int insertRequirement(Requirement requirement) {
        Date now = new Date();
        if (requirement.getCreateTime() == null) {
            requirement.setCreateTime(now);
        }
        if (requirement.getUpdateTime() == null) {
            requirement.setUpdateTime(now);
        }
        return requirementMapper.insert(requirement);
    }
    
    @Override
    public int updateRequirement(Requirement requirement) {
        requirement.setUpdateTime(new Date());
        return requirementMapper.updateById(requirement);
    }
    
    @Override
    public int deleteRequirementByIds(Long[] requirementIds) {
        return requirementMapper.deleteBatchIds(java.util.Arrays.asList(requirementIds));
    }
    
    @Override
    public int deleteRequirementById(Long requirementId) {
        return requirementMapper.deleteById(requirementId);
    }
    
    @Override
    public List<Requirement> selectParentRequirementList(Long personaId, Long canvasId, Long excludeRequirementId) {
        LambdaQueryWrapper<Requirement> wrapper = new LambdaQueryWrapper<>();
        
        // 只查询没有父需求的需求（可以作为父需求）
        wrapper.isNull(Requirement::getParentRequirementId);
        
        // 用户画像ID查询
        if (personaId != null) {
            wrapper.eq(Requirement::getPersonaId, personaId);
        }
        
        // 画布ID查询
        if (canvasId != null) {
            wrapper.eq(Requirement::getCanvasId, canvasId);
        }
        
        // 排除指定的需求ID（编辑时排除自己，避免循环引用）
        if (excludeRequirementId != null) {
            wrapper.ne(Requirement::getRequirementId, excludeRequirementId);
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Requirement::getCreateTime);
        
        return requirementMapper.selectList(wrapper);
    }
    
    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<Requirement> buildQueryWrapper(Requirement requirement) {
        LambdaQueryWrapper<Requirement> wrapper = new LambdaQueryWrapper<>();
        
        if (requirement != null) {
            // 用户画像ID查询
            if (requirement.getPersonaId() != null) {
                wrapper.eq(Requirement::getPersonaId, requirement.getPersonaId());
            }
            
            // 画布ID查询
            if (requirement.getCanvasId() != null) {
                wrapper.eq(Requirement::getCanvasId, requirement.getCanvasId());
            }
            
            // 标题模糊查询
            if (StringUtils.isNotEmpty(requirement.getTitle())) {
                wrapper.like(Requirement::getTitle, requirement.getTitle());
            }
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Requirement::getCreateTime);
        
        return wrapper;
    }
}

