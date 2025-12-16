package com.gameengine.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.WritingTrainingQuestion;
import com.gameengine.system.domain.WritingTrainingRecord;
import com.gameengine.system.mapper.WritingTrainingQuestionMapper;
import com.gameengine.system.mapper.WritingTrainingRecordMapper;
import com.gameengine.system.service.IWritingTrainingService;

import lombok.extern.slf4j.Slf4j;

/**
 * 写作训练题目与记录 Service 实现
 */
@Slf4j
@Service
public class WritingTrainingServiceImpl implements IWritingTrainingService {

    @Autowired
    private WritingTrainingQuestionMapper questionMapper;

    @Autowired
    private WritingTrainingRecordMapper recordMapper;

    @Override
    public List<WritingTrainingQuestion> saveQuestions(Long userId, String moduleKey, String moduleTitle,
                                                       String difficulty, List<String> questions) {
        if (!StringUtils.hasText(moduleKey)) {
            throw new IllegalArgumentException("moduleKey 不能为空");
        }
        if (CollectionUtils.isEmpty(questions)) {
            throw new IllegalArgumentException("questions 不能为空");
        }

        Date now = new Date();
        List<WritingTrainingQuestion> result = new ArrayList<>();
        for (String q : questions) {
            if (!StringUtils.hasText(q)) {
                continue;
            }
            WritingTrainingQuestion entity = new WritingTrainingQuestion();
            entity.setUserId(userId);
            entity.setModuleKey(moduleKey);
            entity.setModuleTitle(moduleTitle);
            entity.setDifficulty(difficulty);
            entity.setContent(q.trim());
            entity.setCreateTime(now);
            questionMapper.insert(entity);
            result.add(entity);
        }
        return result;
    }

    @Override
    public List<WritingTrainingQuestion> getRandomQuestions(Long userId, String moduleKey, int count) {
        int limit = count <= 0 ? 5 : Math.min(count, 50);
        QueryWrapper<WritingTrainingQuestion> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.and(w -> w.eq("user_id", userId).or().isNull("user_id"));
        }
        if (StringUtils.hasText(moduleKey)) {
            wrapper.eq("module_key", moduleKey);
        }
        wrapper.last("ORDER BY RAND() LIMIT " + limit);
        return questionMapper.selectList(wrapper);
    }

    @Override
    public WritingTrainingRecord saveRecord(Long userId, String moduleKey, String moduleTitle,
                                            String difficulty, Long questionId, String content) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        if (questionId == null) {
            throw new IllegalArgumentException("questionId 不能为空");
        }
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("content 不能为空");
        }

        Date now = new Date();

        // 冗余题目内容，方便前端展示
        String questionContent = null;
        if (questionId != null) {
            WritingTrainingQuestion q = questionMapper.selectById(questionId);
            if (q != null) {
                questionContent = q.getContent();
            }
        }

        WritingTrainingRecord record = new WritingTrainingRecord();
        record.setUserId(userId);
        record.setModuleKey(moduleKey);
        record.setModuleTitle(moduleTitle);
        record.setDifficulty(difficulty);
        record.setQuestionId(questionId);
        record.setQuestionContent(questionContent);
        record.setContent(content);
        record.setCreateTime(now);
        record.setUpdateTime(now);

        recordMapper.insert(record);
        return record;
    }

    @Override
    public IPage<WritingTrainingRecord> getRecordPage(Page<WritingTrainingRecord> page, Long userId,
                                                     String moduleKey) {
        if (userId == null) {
            return Page.of(1, page.getSize());
        }
        QueryWrapper<WritingTrainingRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (StringUtils.hasText(moduleKey)) {
            wrapper.eq("module_key", moduleKey);
        }
        wrapper.orderByDesc("create_time");
        return recordMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<WritingTrainingQuestion> getQuestionPage(Page<WritingTrainingQuestion> page, Long userId,
                                                          String moduleKey, String difficulty) {
        QueryWrapper<WritingTrainingQuestion> wrapper = new QueryWrapper<>();
        if (userId != null) {
            // 当前用户自己的题目 + 公共题库
            wrapper.and(w -> w.eq("user_id", userId).or().isNull("user_id"));
        }
        if (StringUtils.hasText(moduleKey)) {
            wrapper.eq("module_key", moduleKey);
        }
        if (StringUtils.hasText(difficulty)) {
            wrapper.eq("difficulty", difficulty);
        }
        wrapper.orderByDesc("create_time");
        return questionMapper.selectPage(page, wrapper);
    }
}
