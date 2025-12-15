package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.AttentionTextFocusContent;
import com.gameengine.system.domain.AttentionTextFocusRecord;
import com.gameengine.system.domain.dto.TextFocusGenerateRequest;

public interface IAttentionTextFocusService {

    /**
     * 调用模型批量生成内容并入库
     */
    List<AttentionTextFocusContent> generateContents(TextFocusGenerateRequest request, Long userId) throws Exception;

    /**
     * 获取最新的生成内容
     */
    List<AttentionTextFocusContent> getLatestContents(Integer limit, String theme);

    /**
     * 保存用户训练记录
     */
    AttentionTextFocusRecord saveRecord(AttentionTextFocusRecord record);

    /**
     * 查询用户的历史记录
     */
    List<AttentionTextFocusRecord> getRecordsByUserId(Long userId, Integer limit);

    /**
     * 更新指定内容的音频地址
     */
    void updateContentAudioUrl(Long contentId, String audioUrl);
}

