package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.PoetryChallengeRecord;
import com.gameengine.system.domain.PoetryInfo;
import com.gameengine.system.domain.dto.PoetryGenerateRequest;

public interface IPoetryChallengeService {
    
    PoetryChallengeRecord saveRecord(PoetryChallengeRecord record);
    
    List<PoetryChallengeRecord> getTopRecords(Integer limit);
    
    List<PoetryInfo> generatePoetries(PoetryGenerateRequest request, Long userId) throws Exception;
    
    List<PoetryInfo> getLatestPoetries(Integer limit);

    /**
     * 按标签查询已存诗词
     */
    List<PoetryInfo> getPoetriesByTag(String tag, Integer limit);

    /**
     * 获取所有标签（去重）
     */
    List<String> getAllTags();

    /**
     * 获取诗词的视频/讲解检索建议
     */
    String getVideoSuggestion(String title) throws Exception;

    /**
     * 检索诗词：先从数据库查询，没有则调用DeepSeek获取
     * @param title 诗词标题
     * @return 诗词信息
     */
    PoetryInfo searchPoetry(String title) throws Exception;
}

