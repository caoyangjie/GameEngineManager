package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.AttentionNumberFaxRecord;

/**
 * 数字传真训练服务 业务层
 * 
 * @author GameEngine
 */
public interface IAttentionNumberFaxService {
    
    /**
     * 保存训练记录
     * 
     * @param record 训练记录
     * @return 保存后的记录
     */
    AttentionNumberFaxRecord saveRecord(AttentionNumberFaxRecord record);
    
    /**
     * 获取训练记录列表
     * 
     * @param limit 返回记录数，默认10
     * @return 训练记录列表
     */
    List<AttentionNumberFaxRecord> getRecords(Integer limit);
}

