package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.utils.StringUtils;
import com.gameengine.system.domain.Scenario;
import com.gameengine.system.mapper.ScenarioMapper;
import com.gameengine.system.service.IScenarioService;

/**
 * 用户场景 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class ScenarioServiceImpl implements IScenarioService {
    
    @Autowired
    private ScenarioMapper scenarioMapper;
    
    @Override
    public IPage<Scenario> selectScenarioList(Page<Scenario> page, Scenario scenario) {
        LambdaQueryWrapper<Scenario> wrapper = buildQueryWrapper(scenario);
        return scenarioMapper.selectPage(page, wrapper);
    }
    
    @Override
    public List<Scenario> selectScenarioList(Scenario scenario) {
        LambdaQueryWrapper<Scenario> wrapper = buildQueryWrapper(scenario);
        return scenarioMapper.selectList(wrapper);
    }
    
    @Override
    public Scenario selectScenarioById(Long scenarioId) {
        return scenarioMapper.selectById(scenarioId);
    }
    
    @Override
    public int insertScenario(Scenario scenario) {
        Date now = new Date();
        if (scenario.getCreateTime() == null) {
            scenario.setCreateTime(now);
        }
        if (scenario.getUpdateTime() == null) {
            scenario.setUpdateTime(now);
        }
        return scenarioMapper.insert(scenario);
    }
    
    @Override
    public int updateScenario(Scenario scenario) {
        scenario.setUpdateTime(new Date());
        return scenarioMapper.updateById(scenario);
    }
    
    @Override
    public int deleteScenarioByIds(Long[] scenarioIds) {
        return scenarioMapper.deleteBatchIds(java.util.Arrays.asList(scenarioIds));
    }
    
    @Override
    public int deleteScenarioById(Long scenarioId) {
        return scenarioMapper.deleteById(scenarioId);
    }
    
    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<Scenario> buildQueryWrapper(Scenario scenario) {
        LambdaQueryWrapper<Scenario> wrapper = new LambdaQueryWrapper<>();
        
        if (scenario != null) {
            // 用户画像ID查询
            if (scenario.getPersonaId() != null) {
                wrapper.eq(Scenario::getPersonaId, scenario.getPersonaId());
            }
            
            // 画布ID查询
            if (scenario.getCanvasId() != null) {
                wrapper.eq(Scenario::getCanvasId, scenario.getCanvasId());
            }
            
            // 标题模糊查询
            if (StringUtils.isNotEmpty(scenario.getTitle())) {
                wrapper.like(Scenario::getTitle, scenario.getTitle());
            }
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Scenario::getCreateTime);
        
        return wrapper;
    }
}

