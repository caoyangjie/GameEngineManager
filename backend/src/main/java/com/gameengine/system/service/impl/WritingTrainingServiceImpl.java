package com.gameengine.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.WritingTrainingQuestion;
import com.gameengine.system.domain.WritingTrainingRecord;
import com.gameengine.system.domain.dto.DeepSeekChatResponse;
import com.gameengine.system.domain.dto.DeepSeekMessage;
import com.gameengine.system.mapper.WritingTrainingQuestionMapper;
import com.gameengine.system.mapper.WritingTrainingRecordMapper;
import com.gameengine.system.service.IDeepSeekService;
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

    @Autowired
    private IDeepSeekService deepSeekService;

    @Override
    public List<WritingTrainingQuestion> saveQuestions(Long userId, String moduleKey, String moduleTitle,
                                                       String difficulty, List<String> questions,
                                                       List<List<String>> sampleAnswers) {
        if (!StringUtils.hasText(moduleKey)) {
            throw new IllegalArgumentException("moduleKey 不能为空");
        }
        if (CollectionUtils.isEmpty(questions)) {
            throw new IllegalArgumentException("questions 不能为空");
        }

        Date now = new Date();
        List<WritingTrainingQuestion> result = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            String q = questions.get(i);
            if (!StringUtils.hasText(q)) {
                continue;
            }
            WritingTrainingQuestion entity = new WritingTrainingQuestion();
            entity.setUserId(userId);
            entity.setModuleKey(moduleKey);
            entity.setModuleTitle(moduleTitle);
            entity.setDifficulty(difficulty);
            entity.setContent(q.trim());
            // 如果传入了示例答案，则将其保存为 JSON，便于后端/管理端快速预览
            if (sampleAnswers != null && i < sampleAnswers.size()) {
                List<String> samples = sampleAnswers.get(i);
                if (!CollectionUtils.isEmpty(samples)) {
                    entity.setSamplesJson(JSON.toJSONString(samples));
                }
            }
            entity.setCreateTime(now);
            questionMapper.insert(entity);
            result.add(entity);

            // 如果有示例答案，则为当前题目创建示例训练记录
            if (sampleAnswers != null && i < sampleAnswers.size()) {
                List<String> samples = sampleAnswers.get(i);
                if (!CollectionUtils.isEmpty(samples)) {
                    for (String sample : samples) {
                        if (!StringUtils.hasText(sample)) {
                            continue;
                        }
                        WritingTrainingRecord record = new WritingTrainingRecord();
                        record.setUserId(userId);
                        record.setModuleKey(moduleKey);
                        record.setModuleTitle(moduleTitle);
                        record.setDifficulty(difficulty);
                        record.setQuestionId(entity.getId());
                        record.setQuestionContent(entity.getContent());
                        record.setContent(sample.trim());
                        record.setIsExample(Boolean.TRUE);
                        record.setCreateTime(now);
                        record.setUpdateTime(now);
                        recordMapper.insert(record);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<WritingTrainingQuestion> generateQuestions(Long userId, String moduleKey, String moduleTitle,
                                                           String difficulty, int count,
                                                           List<String> expressionTemplates) throws Exception {
        if (!StringUtils.hasText(moduleKey)) {
            throw new IllegalArgumentException("moduleKey 不能为空");
        }
        int size = count <= 0 ? 1 : Math.min(count, 10);
        String finalModuleTitle = StringUtils.hasText(moduleTitle) ? moduleTitle : "写作训练";

        String levelLabel;
        if (!StringUtils.hasText(difficulty)) {
            levelLabel = "通用";
        } else {
            switch (difficulty) {
                case "primary_low":
                    levelLabel = "小学1-3年级";
                    break;
                case "primary_high":
                    levelLabel = "小学4-6年级";
                    break;
                case "middle":
                    levelLabel = "初中1-3年级";
                    break;
                case "high":
                    levelLabel = "高中1-3年级";
                    break;
                default:
                    levelLabel = "通用";
                    break;
            }
        }

        String templatesText = "";
        if (expressionTemplates != null && !expressionTemplates.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("表达模板（示范答案需贴合下列模板句式，可适度改写但需体现模板意图）：\n");
            for (int i = 0; i < expressionTemplates.size(); i++) {
                sb.append(i + 1).append(") ").append(expressionTemplates.get(i)).append("\n");
            }
            templatesText = sb.toString();
        }

        String systemPrompt = "你是一名语文写作教练，请根据指定写作技巧模块为学生生成训练题目及示范答案。" +
                "请为\"" + finalModuleTitle + "\"提供" + size + "道训练题，并为每一道题生成 5 个示范答案。" +
                "根据学生年级调整难度：" + levelLabel + "。" +
                (StringUtils.hasText(templatesText) ? templatesText : "") +
                "示范答案必须显式使用上述表达模板中的句式或结构，不可使用与模板无关的表达。" +
                "请严格按照下面的 JSON 数组格式输出，不要包含任何多余文字或注释：" +
                "[{\"question\":\"题目内容1\",\"samples\":[\"示范答案1\",\"示范答案2\",\"示范答案3\",\"示范答案4\",\"示范答案5\"]}]。" +
                "只输出 JSON 数组，不要输出解释说明。";

        String userContent = "请生成" + size + "道与「" + finalModuleTitle + "」相关的写作练习题，每道题附带 5 个示范答案。" +
                "难度：" + levelLabel + "。请只输出前面说明的 JSON 数组，不要输出其他说明文字。";

        List<DeepSeekMessage> messages = new ArrayList<>();
        messages.add(new DeepSeekMessage("system", systemPrompt));
        messages.add(new DeepSeekMessage("user", userContent));

        DeepSeekChatResponse response = deepSeekService.chat(messages);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new IllegalArgumentException("AI 返回为空，未解析到题目数据");
        }
        String content = response.getChoices().get(0).getMessage() != null
                ? response.getChoices().get(0).getMessage().getContent()
                : null;
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("AI 返回内容为空");
        }

        String jsonText = extractJsonArray(content);
        JSONArray array = JSON.parseArray(jsonText);
        if (array == null || array.isEmpty()) {
            throw new IllegalArgumentException("AI 返回 JSON 为空，未解析到题目");
        }

        List<String> questions = new ArrayList<>();
        List<List<String>> samplesAll = new ArrayList<>();

        for (int i = 0; i < array.size() && questions.size() < size; i++) {
            JSONObject obj = array.getJSONObject(i);
            if (obj == null) {
                continue;
            }
            String question = obj.getString("question");
            if (!StringUtils.hasText(question)) {
                continue;
            }
            questions.add(question.trim());

            JSONArray samplesArr = obj.getJSONArray("samples");
            List<String> oneSamples = new ArrayList<>();
            if (samplesArr != null) {
                for (int j = 0; j < samplesArr.size(); j++) {
                    String sample = samplesArr.getString(j);
                    if (StringUtils.hasText(sample)) {
                        oneSamples.add(sample.trim());
                    }
                }
            }
            samplesAll.add(oneSamples);
        }

        if (CollectionUtils.isEmpty(questions)) {
            throw new IllegalArgumentException("未解析到有效题目");
        }

        return saveQuestions(userId, moduleKey, finalModuleTitle, difficulty, questions, samplesAll);
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
        record.setIsExample(Boolean.FALSE);
        record.setCreateTime(now);
        record.setUpdateTime(now);

        recordMapper.insert(record);
        return record;
    }

    @Override
    public IPage<WritingTrainingRecord> getRecordPage(Page<WritingTrainingRecord> page, Long userId,
                                                     String moduleKey, String difficulty) {
        if (userId == null) {
            return Page.of(1, page.getSize());
        }
        QueryWrapper<WritingTrainingRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (StringUtils.hasText(moduleKey)) {
            wrapper.eq("module_key", moduleKey);
        }
        if (StringUtils.hasText(difficulty)) {
            wrapper.eq("difficulty", difficulty);
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

    @Override
    public List<WritingTrainingRecord> getRecordsByQuestionId(Long userId, Long questionId) {
        if (userId == null || questionId == null) {
            return new ArrayList<>();
        }
        QueryWrapper<WritingTrainingRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("question_id", questionId);
        wrapper.orderByDesc("create_time");
        return recordMapper.selectList(wrapper);
    }

    /**
     * 从原始内容中提取 JSON 数组片段，兼容 ```json 代码块包裹的场景
     */
    private String extractJsonArray(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("模型返回为空");
        }
        int start = raw.indexOf('[');
        int end = raw.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return raw.substring(start, end + 1);
        }
        String cleaned = raw.replace("```json", "").replace("```", "").trim();
        if (cleaned.startsWith("[")) {
            return cleaned;
        }
        throw new IllegalArgumentException("未能解析模型返回的 JSON 数据");
    }
}
