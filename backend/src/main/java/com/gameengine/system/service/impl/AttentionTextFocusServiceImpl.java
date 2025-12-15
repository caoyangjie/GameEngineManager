package com.gameengine.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameengine.system.domain.AttentionTextFocusContent;
import com.gameengine.system.domain.AttentionTextFocusRecord;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.domain.dto.TextFocusGenerateRequest;
import com.gameengine.system.mapper.AttentionTextFocusContentMapper;
import com.gameengine.system.mapper.AttentionTextFocusRecordMapper;
import com.gameengine.system.service.IAttentionTextFocusService;
import com.gameengine.system.service.IDeepSeekService;
import com.gameengine.system.service.ISysUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AttentionTextFocusServiceImpl implements IAttentionTextFocusService {

    private static final String DEFAULT_THEME = "日常生活";

    @Autowired
    private AttentionTextFocusContentMapper contentMapper;

    @Autowired
    private AttentionTextFocusRecordMapper recordMapper;

    @Autowired
    private IDeepSeekService deepSeekService;

    @Autowired
    private ISysUserService userService;

    @Override
    public List<AttentionTextFocusContent> generateContents(TextFocusGenerateRequest request, Long userId) throws Exception {
        int count = request.getCount() != null && request.getCount() > 0 ? Math.min(request.getCount(), 20) : 10;
        String theme = StringUtils.hasText(request.getTheme()) ? request.getTheme().trim() : DEFAULT_THEME;
        String level = normalizeLevel(request.getLevel());

        String systemPrompt = "你是专注力训练出题助手，请严格返回 JSON。" +
                "根据指定难度生成 100-150 字中文短文，场景与日常生活相关；" +
                "为短文设计 5 个问题并给出标准答案，覆盖主旨、细节、情感/态度、推断等维度；" +
                "难度要求：初=小学1-3年级，中=小学3-6年级，高=初中1-3年级，困=高中1-3年级。" +
                "题目用词与推理深度需符合难度，避免超纲。" +
                "输出格式：[{\"title\":\"日常情景1\",\"paragraph\":\"……\",\"questions\":[{\"question\":\"?\",\"answer\":\"…\"},...]},...]。" +
                "不要返回除 JSON 外的任何文字。";

        String userMessage = String.format(
                "请一次性生成 %d 条“%s”主题的文字专注力训练素材，短文 100-150 字，难度等级：%s（初=小学1-3年级，中=小学3-6年级，高=初中1-3年级，困=高中1-3年级）。",
                count, theme, level);

        String response = deepSeekService.chatSimple(userMessage, systemPrompt);
        String jsonPayload = extractJsonArray(response);

        JSONArray items = JSON.parseArray(jsonPayload);
        Date now = new Date();
        List<AttentionTextFocusContent> saved = new ArrayList<>();

        String username = "匿名用户";
        if (userId != null) {
            SysUser user = userService.selectUserById(userId);
            if (user != null) {
                username = StringUtils.hasText(user.getNickName()) ? user.getNickName() : user.getUserName();
                if (!StringUtils.hasText(username)) {
                    username = "匿名用户";
                }
            }
        }

        for (int i = 0; i < items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item == null) {
                continue;
            }
            String paragraph = item.getString("paragraph");
            JSONArray questions = item.getJSONArray("questions");
            if (!StringUtils.hasText(paragraph) || questions == null || questions.isEmpty()) {
                log.warn("跳过无效的生成项：{}", item);
                continue;
            }

            String title = item.getString("title");
            if (!StringUtils.hasText(title)) {
                title = String.format("%s情景练习 %d", theme, i + 1);
            }

            AttentionTextFocusContent content = new AttentionTextFocusContent();
            content.setParagraph(paragraph.trim());
            content.setQuestionsJson(questions.toJSONString());
            content.setTheme(theme);
            content.setTitle(title);
            content.setSourceModel("deepseek");
            content.setLevel(level);
            content.setCreateTime(now);
            content.setUpdateTime(now);
            content.setUserId(userId);
            content.setUsername(username);

            contentMapper.insert(content);
            saved.add(content);
        }

        return saved;
    }

    @Override
    public List<AttentionTextFocusContent> getLatestContents(Integer limit, String theme) {
        int pageSize = (limit == null || limit <= 0) ? 10 : Math.min(limit, 50);
        QueryWrapper<AttentionTextFocusContent> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(theme)) {
            wrapper.eq("theme", theme.trim());
        }
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT " + pageSize);
        return contentMapper.selectList(wrapper);
    }

    @Override
    public AttentionTextFocusRecord saveRecord(AttentionTextFocusRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("记录不能为空");
        }
        Date now = new Date();
        if (record.getTotalQuestions() != null && record.getCorrectCount() != null && record.getAccuracy() == null) {
            int total = Math.max(1, record.getTotalQuestions());
            int accuracy = (int) Math.round((record.getCorrectCount() * 100.0) / total);
            record.setAccuracy(accuracy);
        }
        record.setCreateTime(now);
        record.setUpdateTime(now);
        recordMapper.insert(record);
        return record;
    }

    @Override
    public List<AttentionTextFocusRecord> getRecordsByUserId(Long userId, Integer limit) {
        if (userId == null) {
            return new ArrayList<>();
        }
        int pageSize = (limit == null || limit <= 0) ? 10 : Math.min(limit, 50);
        QueryWrapper<AttentionTextFocusRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT " + pageSize);
        return recordMapper.selectList(wrapper);
    }

    private String extractJsonArray(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("模型返回为空");
        }
        int start = raw.indexOf('[');
        int end = raw.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return raw.substring(start, end + 1);
        }
        // 尝试去除代码块标记
        String cleaned = raw.replace("```json", "").replace("```", "").trim();
        if (cleaned.startsWith("[")) {
            return cleaned;
        }
        throw new IllegalArgumentException("未能解析模型返回的 JSON 数据");
    }

    private String normalizeLevel(String input) {
        if (!StringUtils.hasText(input)) {
            return "中";
        }
        String trimmed = input.trim();
        switch (trimmed) {
            case "初":
            case "初级":
                return "初";
            case "中":
            case "中级":
                return "中";
            case "高":
            case "高级":
                return "高";
            case "困":
            case "困难":
                return "困";
            default:
                return "中";
        }
    }
}

