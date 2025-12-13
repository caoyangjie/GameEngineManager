package com.gameengine.system.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.AttentionSensoryMemoryContent;
import com.gameengine.system.domain.AttentionSensoryMemoryRecord;
import com.gameengine.system.domain.dto.DeepSeekChatResponse;
import com.gameengine.system.domain.dto.DeepSeekMessage;
import com.gameengine.system.domain.dto.SensoryMemoryGenerateRequest;
import com.gameengine.system.mapper.AttentionSensoryMemoryContentMapper;
import com.gameengine.system.mapper.AttentionSensoryMemoryRecordMapper;
import com.gameengine.system.service.IDeepSeekService;
import com.gameengine.system.service.ISensoryMemoryService;

/**
 * 感官探险记忆法服务实现
 */
@Service
public class SensoryMemoryServiceImpl implements ISensoryMemoryService {
    
    private static final Logger log = LoggerFactory.getLogger(SensoryMemoryServiceImpl.class);
    
    private static final String SYSTEM_PROMPT = "你是一名记忆训练教练，擅长通过多感官协同来增强记忆。请按照用户提供的主题/难度生成可用于感官记忆训练的词语或句子列表，并\"仅\"返回JSON数组，禁止输出任何Markdown或解释。每个对象需要包含字段：content（词语或句子）, description（内容描述）, visualUrl（视觉资源描述，如：\"一张展示XX的图片\"或\"一段XX的视频\"）, audioUrl（听觉资源描述，如：\"XX的音乐\"或\"XX的自然声音\"）, scentDescription（嗅觉描述，如：\"花香\"、\"咖啡香\"等）, touchDescription（触觉描述，如：\"光滑的石头\"、\"柔软的布料\"等）, tasteDescription（味觉描述，如：\"甜美的水果\"、\"香醇的咖啡\"等）, tags（逗号分隔标签，3-5个）。要求：1) 内容与主题贴合；2) 感官体验描述具体生动；3) 便于记忆和联想。直接返回JSON数组即可。";
    
    private static final String PROMPT_TEMPLATE_PATH = "prompts/sensory_memory_template.txt";
    private static String promptTemplate = null;
    
    @Autowired
    private AttentionSensoryMemoryRecordMapper recordMapper;
    
    @Autowired
    private AttentionSensoryMemoryContentMapper contentMapper;
    
    @Autowired
    private IDeepSeekService deepSeekService;
    
    /**
     * 加载提示词模板
     */
    private synchronized String loadPromptTemplate() {
        if (promptTemplate != null) {
            return promptTemplate;
        }
        
        try {
            ClassPathResource resource = new ClassPathResource(PROMPT_TEMPLATE_PATH);
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            inputStream.close();
            promptTemplate = sb.toString();
            log.info("成功加载感官记忆提示词模板");
        } catch (Exception e) {
            log.error("加载提示词模板失败，使用默认模板", e);
            // 使用默认模板
            promptTemplate = "主题：{主题}\n\n{关键词标签}\n\n{标签行}\n\n请基于上述内容生成与以下感官相关的{内容类型}：\n\n1. 视觉：请提供与主题相关的图像、颜色、形状等{内容类型}。\n\n2. 听觉：请生成与主题相关的声音、音乐、自然声音等{内容类型}。\n\n3. 嗅觉：请列出与主题相关的气味、香气、味道等{内容类型}。\n\n4. 触觉：请提供与主题相关的质感、温度、材质等{内容类型}。\n\n5. 味觉：请生成与主题相关的食物、饮品、味道等{内容类型}。\n\n请确保生成的内容丰富且富有创意，能够帮助记忆和联想。\n";
        }
        
        return promptTemplate;
    }
    
    @Override
    public AttentionSensoryMemoryRecord saveRecord(AttentionSensoryMemoryRecord record) {
        Date now = new Date();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        
        if (record.getContentCount() == null) {
            record.setContentCount(0);
        }
        if (record.getMemorizeDurationSeconds() == null) {
            record.setMemorizeDurationSeconds(0);
        }
        if (record.getRecallDurationSeconds() == null) {
            record.setRecallDurationSeconds(0);
        }
        if (record.getTotalDurationSeconds() == null) {
            int memorize = record.getMemorizeDurationSeconds() == null ? 0 : record.getMemorizeDurationSeconds();
            int recall = record.getRecallDurationSeconds() == null ? 0 : record.getRecallDurationSeconds();
            record.setTotalDurationSeconds(memorize + recall);
        }
        if (record.getCorrectCount() == null) {
            record.setCorrectCount(0);
        }
        if (record.getAccuracy() == null) {
            int total = record.getContentCount() == null ? 0 : record.getContentCount();
            int correct = record.getCorrectCount() == null ? 0 : record.getCorrectCount();
            int accuracy = total > 0 ? (int) Math.round(correct * 100.0 / total) : 0;
            record.setAccuracy(accuracy);
        }
        
        recordMapper.insert(record);
        return record;
    }
    
    @Override
    public List<AttentionSensoryMemoryRecord> getTopRecords(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        QueryWrapper<AttentionSensoryMemoryRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("accuracy").orderByDesc("correct_count").last("LIMIT " + limit);
        return recordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AttentionSensoryMemoryContent> generateContents(SensoryMemoryGenerateRequest request, Long userId) throws Exception {
        String contentType = StringUtils.hasText(request.getContentType()) ? request.getContentType() : "word";
        
        String userPrompt = buildUserPrompt(request, contentType);
        
        List<DeepSeekMessage> messages = new ArrayList<>();
        messages.add(new DeepSeekMessage("system", SYSTEM_PROMPT));
        messages.add(new DeepSeekMessage("user", userPrompt));
        
        DeepSeekChatResponse response = deepSeekService.chat(messages);
        String content = response != null
                && response.getChoices() != null
                && !response.getChoices().isEmpty()
                && response.getChoices().get(0).getMessage() != null
                ? response.getChoices().get(0).getMessage().getContent()
                : "[]";
        
        String json = extractJsonArray(content);
        JSONArray array = JSON.parseArray(json);
        List<AttentionSensoryMemoryContent> contents = new ArrayList<>();
        
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            AttentionSensoryMemoryContent contentEntity = new AttentionSensoryMemoryContent();
            contentEntity.setContentType(contentType);
            contentEntity.setContent(obj.getString("content"));
            contentEntity.setDescription(obj.getString("description"));
            contentEntity.setVisualUrl(obj.getString("visualUrl"));
            contentEntity.setAudioUrl(obj.getString("audioUrl"));
            contentEntity.setScentDescription(obj.getString("scentDescription"));
            contentEntity.setTouchDescription(obj.getString("touchDescription"));
            contentEntity.setTasteDescription(obj.getString("tasteDescription"));
            
            String tags = obj.getString("tags");
            if (StringUtils.hasText(tags)) {
                contentEntity.setTags(tags);
            }
            
            contentEntity.setCreatedBy(userId);
            Date now = new Date();
            contentEntity.setCreateTime(now);
            contentEntity.setUpdateTime(now);
            
            try {
                contentMapper.insert(contentEntity);
                contents.add(contentEntity);
            } catch (DataIntegrityViolationException e) {
                log.warn("插入感官记忆内容时发生数据完整性错误，跳过：{}", e.getMessage());
            } catch (Exception e) {
                log.error("插入感官记忆内容失败", e);
            }
        }
        
        return contents;
    }
    
    @Override
    public List<AttentionSensoryMemoryContent> getLatestContents(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        QueryWrapper<AttentionSensoryMemoryContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time").last("LIMIT " + limit);
        return contentMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AttentionSensoryMemoryContent> getContentsByTag(String tag, Integer limit) {
        if (!StringUtils.hasText(tag)) {
            return new ArrayList<>();
        }
        if (limit == null || limit <= 0) {
            limit = 30;
        }
        QueryWrapper<AttentionSensoryMemoryContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("tags", tag);
        queryWrapper.orderByDesc("create_time").last("LIMIT " + limit);
        return contentMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<String> getAllTags() {
        List<AttentionSensoryMemoryContent> all = contentMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(all)) {
            return new ArrayList<>();
        }
        return all.stream()
                .filter(content -> StringUtils.hasText(content.getTags()))
                .flatMap(content -> Stream.of(content.getTags().split("[,，]")))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AttentionSensoryMemoryRecord> getRecordsByUserId(Long userId, Integer pageNum, Integer pageSize) {
        if (userId == null) {
            return new ArrayList<>();
        }
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        
        QueryWrapper<AttentionSensoryMemoryRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        
        Page<AttentionSensoryMemoryRecord> page = new Page<>(pageNum, pageSize);
        Page<AttentionSensoryMemoryRecord> result = recordMapper.selectPage(page, queryWrapper);
        return result.getRecords();
    }
    
    /**
     * 构建用户提示词，使用模板文件
     */
    private String buildUserPrompt(SensoryMemoryGenerateRequest request, String contentType) {
        String template = loadPromptTemplate();
        String contentTypeText = "word".equals(contentType) ? "词语" : "句子";
        
        // 替换主题
        String prompt = StringUtils.hasText(request.getPrompt()) ? request.getPrompt() : "";
        template = template.replace("{主题}", prompt);
        
        // 替换关键词标签行
        String keywordsLabel = "";
        if (StringUtils.hasText(request.getKeywords())) {
            String label = "word".equals(contentType) ? "关键词" : "句子";
            keywordsLabel = label + "：" + request.getKeywords();
        }
        template = template.replace("{关键词标签}", keywordsLabel);
        
        // 替换标签行
        String tagsLine = "";
        if (!CollectionUtils.isEmpty(request.getTags())) {
            tagsLine = "标签：" + String.join("、", request.getTags());
        }
        template = template.replace("{标签行}", tagsLine);
        
        // 替换内容类型
        template = template.replace("{内容类型}", contentTypeText);
        
        // 清理多余的空行（连续两个换行保留一个）
        template = template.replaceAll("\n{3,}", "\n\n");
        
        return template.trim();
    }
    
    private String extractJsonArray(String text) {
        if (!StringUtils.hasText(text)) {
            return "[]";
        }
        text = text.trim();
        
        int start = text.indexOf('[');
        int end = text.lastIndexOf(']');
        
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        
        return "[]";
    }
}

