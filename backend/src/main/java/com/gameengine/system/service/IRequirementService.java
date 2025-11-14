package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.Requirement;

/**
 * 用户需求 业务层
 * 
 * @author GameEngine
 */
public interface IRequirementService {
    
    /**
     * 查询用户需求列表（分页）
     * 
     * @param page 分页对象
     * @param requirement 查询条件
     * @return 用户需求列表
     */
    IPage<Requirement> selectRequirementList(Page<Requirement> page, Requirement requirement);
    
    /**
     * 查询用户需求列表（不分页）
     * 
     * @param requirement 查询条件
     * @return 用户需求列表
     */
    List<Requirement> selectRequirementList(Requirement requirement);
    
    /**
     * 根据ID查询用户需求
     * 
     * @param requirementId 用户需求ID
     * @return 用户需求对象
     */
    Requirement selectRequirementById(Long requirementId);
    
    /**
     * 新增用户需求
     * 
     * @param requirement 用户需求对象
     * @return 结果
     */
    int insertRequirement(Requirement requirement);
    
    /**
     * 修改用户需求
     * 
     * @param requirement 用户需求对象
     * @return 结果
     */
    int updateRequirement(Requirement requirement);
    
    /**
     * 批量删除用户需求
     * 
     * @param requirementIds 需要删除的用户需求ID数组
     * @return 结果
     */
    int deleteRequirementByIds(Long[] requirementIds);
    
    /**
     * 删除用户需求信息
     * 
     * @param requirementId 用户需求ID
     * @return 结果
     */
    int deleteRequirementById(Long requirementId);
    
    /**
     * 查询父需求列表（用于需求拆分，只返回没有父需求的需求）
     * 
     * @param personaId 用户画像ID
     * @param canvasId 画布ID
     * @param excludeRequirementId 排除的需求ID（编辑时排除自己）
     * @return 父需求列表
     */
    List<Requirement> selectParentRequirementList(Long personaId, Long canvasId, Long excludeRequirementId);
}

