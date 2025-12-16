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
                                                String difficulty, List<String> questions,
                                                List<List<String>> sampleAnswers);

    /**
     * 调用 DeepSeek 批量生成写作训练题目并保存
     *
     * @param userId      当前用户 ID（可为空，为空时题目视为公共题库）
     * @param moduleKey   模块路由键
     * @param moduleTitle 模块标题
     * @param difficulty  难度编码：primary_low/primary_high/middle/high
     * @param count       生成题目数量，1-10
     * @return 已保存的题目列表
     */
    List<WritingTrainingQuestion> generateQuestions(Long userId, String moduleKey, String moduleTitle,
                                                    String difficulty, int count,
                                                    List<String> expressionTemplates) throws Exception;

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
                                               String moduleKey, String difficulty);

    /**
     * 分页查询写作训练题目
     */
    IPage<WritingTrainingQuestion> getQuestionPage(Page<WritingTrainingQuestion> page, Long userId,
                                                   String moduleKey, String difficulty);

    /**
     * 根据题目 ID 查询该题目的所有训练记录（当前用户）
     */
    List<WritingTrainingRecord> getRecordsByQuestionId(Long userId, Long questionId);
}
