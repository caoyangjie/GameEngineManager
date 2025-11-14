package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.Scenario;

/**
 * 用户场景 业务层
 * 
 * @author GameEngine
 */
public interface IScenarioService {
    
    /**
     * 查询用户场景列表（分页）
     * 
     * @param page 分页对象
     * @param scenario 查询条件
     * @return 用户场景列表
     */
    IPage<Scenario> selectScenarioList(Page<Scenario> page, Scenario scenario);
    
    /**
     * 查询用户场景列表（不分页）
     * 
     * @param scenario 查询条件
     * @return 用户场景列表
     */
    List<Scenario> selectScenarioList(Scenario scenario);
    
    /**
     * 根据ID查询用户场景
     * 
     * @param scenarioId 用户场景ID
     * @return 用户场景对象
     */
    Scenario selectScenarioById(Long scenarioId);
    
    /**
     * 新增用户场景
     * 
     * @param scenario 用户场景对象
     * @return 结果
     */
    int insertScenario(Scenario scenario);
    
    /**
     * 修改用户场景
     * 
     * @param scenario 用户场景对象
     * @return 结果
     */
    int updateScenario(Scenario scenario);
    
    /**
     * 批量删除用户场景
     * 
     * @param scenarioIds 需要删除的用户场景ID数组
     * @return 结果
     */
    int deleteScenarioByIds(Long[] scenarioIds);
    
    /**
     * 删除用户场景信息
     * 
     * @param scenarioId 用户场景ID
     * @return 结果
     */
    int deleteScenarioById(Long scenarioId);
}

