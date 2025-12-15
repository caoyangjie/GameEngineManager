package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.LearningMethodologyTemplate;

/**
 * 教育学习方法论模板 业务层
 * 
 * @author GameEngine
 */
public interface ILearningMethodologyTemplateService {
    
    /**
     * 查询模板列表（分页）
     * 
     * @param page 分页对象
     * @param template 查询条件
     * @return 模板列表
     */
    IPage<LearningMethodologyTemplate> selectTemplateList(Page<LearningMethodologyTemplate> page, LearningMethodologyTemplate template);
    
    /**
     * 查询模板列表（不分页）
     * 
     * @param template 查询条件
     * @return 模板列表
     */
    List<LearningMethodologyTemplate> selectTemplateList(LearningMethodologyTemplate template);
    
    /**
     * 根据ID查询模板
     * 
     * @param templateId 模板ID
     * @return 模板对象
     */
    LearningMethodologyTemplate selectTemplateById(Long templateId);
    
    /**
     * 新增模板
     * 
     * @param template 模板对象
     * @return 结果
     */
    int insertTemplate(LearningMethodologyTemplate template);
    
    /**
     * 修改模板
     * 
     * @param template 模板对象
     * @return 结果
     */
    int updateTemplate(LearningMethodologyTemplate template);
    
    /**
     * 批量删除模板
     * 
     * @param templateIds 需要删除的模板ID数组
     * @return 结果
     */
    int deleteTemplateByIds(Long[] templateIds);
    
    /**
     * 删除模板信息
     * 
     * @param templateId 模板ID
     * @return 结果
     */
    int deleteTemplateById(Long templateId);
}

