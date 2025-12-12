package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.AttentionIdiomAdvancedRecord;
import com.gameengine.system.domain.AttentionIdiomInfo;
import com.gameengine.system.domain.dto.IdiomGenerateRequest;

public interface IAttentionIdiomAdvancedService {
    
    AttentionIdiomAdvancedRecord saveRecord(AttentionIdiomAdvancedRecord record);
    
    List<AttentionIdiomAdvancedRecord> getTopRecords(Integer limit);
    
    List<AttentionIdiomInfo> generateIdioms(IdiomGenerateRequest request, Long userId) throws Exception;
    
    List<AttentionIdiomInfo> getLatestIdioms(Integer limit);

    /**
     * 按标签查询已存成语
     */
    List<AttentionIdiomInfo> getIdiomsByTag(String tag, Integer limit);

    /**
     * 获取所有标签（去重）
     */
    List<String> getAllTags();

    /**
     * 获取成语的视频/讲解检索建议
     */
    String getVideoSuggestion(String idiom) throws Exception;

    /**
     * 检索成语：先从数据库查询，没有则调用DeepSeek获取
     * @param idiom 成语文本
     * @return 成语信息
     */
    AttentionIdiomInfo searchIdiom(String idiom) throws Exception;
}


