package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.Persona;

/**
 * 用户画像 业务层
 * 
 * @author GameEngine
 */
public interface IPersonaService {
    
    /**
     * 查询用户画像列表（分页）
     * 
     * @param page 分页对象
     * @param persona 查询条件
     * @return 用户画像列表
     */
    IPage<Persona> selectPersonaList(Page<Persona> page, Persona persona);
    
    /**
     * 查询用户画像列表（不分页）
     * 
     * @param persona 查询条件
     * @return 用户画像列表
     */
    List<Persona> selectPersonaList(Persona persona);
    
    /**
     * 根据ID查询用户画像
     * 
     * @param personaId 用户画像ID
     * @return 用户画像对象
     */
    Persona selectPersonaById(Long personaId);
    
    /**
     * 新增用户画像
     * 
     * @param persona 用户画像对象
     * @return 结果
     */
    int insertPersona(Persona persona);
    
    /**
     * 修改用户画像
     * 
     * @param persona 用户画像对象
     * @return 结果
     */
    int updatePersona(Persona persona);
    
    /**
     * 批量删除用户画像
     * 
     * @param personaIds 需要删除的用户画像ID数组
     * @return 结果
     */
    int deletePersonaByIds(Long[] personaIds);
    
    /**
     * 删除用户画像信息
     * 
     * @param personaId 用户画像ID
     * @return 结果
     */
    int deletePersonaById(Long personaId);
}

