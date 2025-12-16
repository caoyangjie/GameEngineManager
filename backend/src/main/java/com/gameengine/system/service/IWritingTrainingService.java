package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.WritingTrainingQuestion;
import com.gameengine.system.domain.WritingTrainingRecord;

/**
 * 写作训练题目与记录 服务接口
 */
public interface IWritingTrainingService {

    /**
     * 批量保存 AI 生成的题目
     */
    List<WritingTrainingQuestion> saveQuestions(Long userId, String moduleKey, String moduleTitle,
                                                String difficulty, List<String> questions);

    /**
     * 随机获取若干条题目
     */
    List<WritingTrainingQuestion> getRandomQuestions(Long userId, String moduleKey, int count);

    /**
     * 保存一条写作训练记录
     */
    WritingTrainingRecord saveRecord(Long userId, String moduleKey, String moduleTitle,
                                     String difficulty, Long questionId, String content);

    /**
     * 分页查询写作训练记录
     */
    IPage<WritingTrainingRecord> getRecordPage(Page<WritingTrainingRecord> page, Long userId,
                                               String moduleKey);

    /**
     * 分页查询写作训练题目
     */
    IPage<WritingTrainingQuestion> getQuestionPage(Page<WritingTrainingQuestion> page, Long userId,
                                                   String moduleKey, String difficulty);
}
