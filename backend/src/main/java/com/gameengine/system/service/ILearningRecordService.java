package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.LearningRecord;

/**
 * 学习记录 业务层
 * 
 * @author GameEngine
 */
public interface ILearningRecordService {
    
    /**
     * 查询学习记录列表（分页）
     * 
     * @param page 分页对象
     * @param record 查询条件
     * @param userId 用户ID
     * @return 学习记录列表
     */
    IPage<LearningRecord> selectRecordList(Page<LearningRecord> page, LearningRecord record, Long userId);
    
    /**
     * 查询学习记录列表（不分页）
     * 
     * @param record 查询条件
     * @param userId 用户ID
     * @return 学习记录列表
     */
    List<LearningRecord> selectRecordList(LearningRecord record, Long userId);
    
    /**
     * 根据ID查询学习记录
     * 
     * @param recordId 记录ID
     * @param userId 用户ID
     * @return 学习记录对象
     */
    LearningRecord selectRecordById(Long recordId, Long userId);
    
    /**
     * 新增学习记录
     * 
     * @param record 学习记录对象
     * @return 结果
     */
    int insertRecord(LearningRecord record);
    
    /**
     * 修改学习记录
     * 
     * @param record 学习记录对象
     * @return 结果
     */
    int updateRecord(LearningRecord record);
    
    /**
     * 批量删除学习记录
     * 
     * @param recordIds 需要删除的记录ID数组
     * @param userId 用户ID
     * @return 结果
     */
    int deleteRecordByIds(Long[] recordIds, Long userId);
    
    /**
     * 删除学习记录信息
     * 
     * @param recordId 记录ID
     * @param userId 用户ID
     * @return 结果
     */
    int deleteRecordById(Long recordId, Long userId);
}

