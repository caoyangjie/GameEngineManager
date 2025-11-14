package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.utils.StringUtils;
import com.gameengine.system.domain.Persona;
import com.gameengine.system.mapper.PersonaMapper;
import com.gameengine.system.service.IPersonaService;

/**
 * 用户画像 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class PersonaServiceImpl implements IPersonaService {
    
    @Autowired
    private PersonaMapper personaMapper;
    
    @Override
    public IPage<Persona> selectPersonaList(Page<Persona> page, Persona persona) {
        LambdaQueryWrapper<Persona> wrapper = buildQueryWrapper(persona);
        return personaMapper.selectPage(page, wrapper);
    }
    
    @Override
    public List<Persona> selectPersonaList(Persona persona) {
        LambdaQueryWrapper<Persona> wrapper = buildQueryWrapper(persona);
        return personaMapper.selectList(wrapper);
    }
    
    @Override
    public Persona selectPersonaById(Long personaId) {
        return personaMapper.selectById(personaId);
    }
    
    @Override
    public int insertPersona(Persona persona) {
        Date now = new Date();
        if (persona.getCreateTime() == null) {
            persona.setCreateTime(now);
        }
        if (persona.getUpdateTime() == null) {
            persona.setUpdateTime(now);
        }
        return personaMapper.insert(persona);
    }
    
    @Override
    public int updatePersona(Persona persona) {
        persona.setUpdateTime(new Date());
        return personaMapper.updateById(persona);
    }
    
    @Override
    public int deletePersonaByIds(Long[] personaIds) {
        return personaMapper.deleteBatchIds(java.util.Arrays.asList(personaIds));
    }
    
    @Override
    public int deletePersonaById(Long personaId) {
        return personaMapper.deleteById(personaId);
    }
    
    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<Persona> buildQueryWrapper(Persona persona) {
        LambdaQueryWrapper<Persona> wrapper = new LambdaQueryWrapper<>();
        
        if (persona != null) {
            // 画布ID查询
            if (persona.getCanvasId() != null) {
                wrapper.eq(Persona::getCanvasId, persona.getCanvasId());
            }
            
            // 姓名模糊查询
            if (StringUtils.isNotEmpty(persona.getName())) {
                wrapper.like(Persona::getName, persona.getName());
            }
            
            // 性别查询
            if (StringUtils.isNotEmpty(persona.getGender())) {
                wrapper.eq(Persona::getGender, persona.getGender());
            }
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Persona::getCreateTime);
        
        return wrapper;
    }
}

