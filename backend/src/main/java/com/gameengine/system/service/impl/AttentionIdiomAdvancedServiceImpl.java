package com.gameengine.system.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameengine.common.utils.ResourcesContstants;
import com.gameengine.system.domain.AttentionIdiomAdvancedRecord;
import com.gameengine.system.domain.AttentionIdiomInfo;
import com.gameengine.system.domain.dto.DeepSeekChatResponse;
import com.gameengine.system.domain.dto.DeepSeekMessage;
import com.gameengine.system.domain.dto.IdiomGenerateRequest;
import com.gameengine.system.mapper.AttentionIdiomAdvancedRecordMapper;
import com.gameengine.system.mapper.AttentionIdiomInfoMapper;
import com.gameengine.system.service.IAttentionIdiomAdvancedService;
import com.gameengine.system.service.IDeepSeekService;

/**
 * 成语进阶记忆服务实现
 */
@Service
public class AttentionIdiomAdvancedServiceImpl implements IAttentionIdiomAdvancedService {
    
    private static final String SYSTEM_PROMPT = "你是一名成语学习与记忆教练。请按照用户提供的主题/难度生成可用于记忆训练的成语列表，并\"仅\"返回JSON数组，禁止输出任何Markdown或解释。每个对象需要包含字段：idiom, pinyin, literalMeaning, meaning, moral, originDynasty, originSource, background, story, protagonist, relatedPersons, usage, memoryCues, tags（逗号分隔标签，3-8个）。要求：1) idiom 为常用成语且与主题贴合；2) story与background简明扼要，突出记忆点；3) usage 给出现代使用提示或例句；4) memoryCues 给出联想或谐音线索。直接返回JSON数组即可。";
    
    @Autowired
    private AttentionIdiomAdvancedRecordMapper recordMapper;
    
    @Autowired
    private AttentionIdiomInfoMapper infoMapper;
    
    @Autowired
    private IDeepSeekService deepSeekService;
    
    @Override
    public AttentionIdiomAdvancedRecord saveRecord(AttentionIdiomAdvancedRecord record) {
        Date now = new Date();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        
        if (record.getCorrectCount() == null) {
            record.setCorrectCount(0);
        }
        if (record.getTotalCount() == null) {
            record.setTotalCount(0);
        }
        if (record.getDurationSeconds() == null) {
            record.setDurationSeconds(0);
        }
        if (record.getAccuracy() == null) {
            int total = record.getTotalCount() == null ? 0 : record.getTotalCount();
            int correct = record.getCorrectCount() == null ? 0 : record.getCorrectCount();
            int accuracy = total > 0 ? (int) Math.round(correct * 100.0 / total) : 0;
            record.setAccuracy(accuracy);
        }
        
        recordMapper.insert(record);
        return record;
    }
    
    @Override
    public List<AttentionIdiomAdvancedRecord> getTopRecords(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        QueryWrapper<AttentionIdiomAdvancedRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("accuracy").orderByDesc("correct_count").last("LIMIT " + limit);
        return recordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AttentionIdiomInfo> generateIdioms(IdiomGenerateRequest request, Long userId) throws Exception {
        int count = request.getCount() == null ? 6 : request.getCount();
        count = Math.max(3, Math.min(count, 15));
        
        String userPrompt = buildUserPrompt(request, count);
        
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
        List<AttentionIdiomInfo> idioms = new ArrayList<>();
        try {
            List<AttentionIdiomInfo> parsed = JSON.parseArray(json, AttentionIdiomInfo.class);
            if (!CollectionUtils.isEmpty(parsed)) {
                idioms = parsed;
            }
        } catch (Exception e) {
            // fallback to empty list
        }
        
        Date now = new Date();
        List<AttentionIdiomInfo> limitedList = idioms.stream().filter(Objects::nonNull).limit(count).collect(Collectors.toList());
        for (AttentionIdiomInfo idiom : limitedList) {
            if (idiom.getIdiom() != null) {
                idiom.setIdiom(idiom.getIdiom().trim());
            }
            idiom.setCreatedBy(userId);
            idiom.setUpdateTime(now);
            if (idiom.getCreateTime() == null) {
                idiom.setCreateTime(now);
            }
            
            // upsert by idiom text
            if (StringUtils.hasText(idiom.getIdiom())) {
                QueryWrapper<AttentionIdiomInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("idiom", idiom.getIdiom());
                AttentionIdiomInfo existing = infoMapper.selectOne(queryWrapper);
                if (existing != null) {
                    idiom.setId(existing.getId());
                    idiom.setCreateTime(existing.getCreateTime());
                    infoMapper.updateById(idiom);
                } else {
                    infoMapper.insert(idiom);
                }
            }
        }
        
        return limitedList;
    }
    
    @Override
    public List<AttentionIdiomInfo> getLatestIdioms(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 8;
        }
        QueryWrapper<AttentionIdiomInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time").last("LIMIT " + limit);
        return infoMapper.selectList(queryWrapper);
    }

    @Override
    public List<AttentionIdiomInfo> getIdiomsByTag(String tag, Integer limit) {
        if (!StringUtils.hasText(tag)) {
            return new ArrayList<>();
        }
        if (limit == null || limit <= 0) {
            limit = 30;
        }
        QueryWrapper<AttentionIdiomInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("tags", tag).orderByDesc("update_time").last("LIMIT " + limit);
        return infoMapper.selectList(queryWrapper);
    }

    @Override
    public List<String> getAllTags() {
        List<AttentionIdiomInfo> all = infoMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(all)) {
            return new ArrayList<>();
        }
        return all.stream()
                .filter(info -> StringUtils.hasText(info.getTags()))
                .flatMap(info -> Stream.of(info.getTags().split("[,，]")))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public String getVideoSuggestion(String idiom) throws Exception {
        if (!StringUtils.hasText(idiom)) {
            return "未提供成语，无法生成视频建议";
        }

        String systemPrompt = "你是一名成语讲解视频策划助手，擅长给出视频讲解的结构和搜索关键词。输出简短、可执行的建议。";
        String userPrompt = "请为成语【" + idiom + "】提供1-2个讲解视频的创意方向，" +
                "给出可搜索的中文标题/关键词，以及30秒以内的讲解提纲（突出背景故事、释义、记忆法）。" +
                "请精简输出，避免多余修饰。";

        List<DeepSeekMessage> messages = new ArrayList<>();
        messages.add(new DeepSeekMessage("system", systemPrompt));
        messages.add(new DeepSeekMessage("user", userPrompt));

        DeepSeekChatResponse response = deepSeekService.chat(messages);
        if (response != null
                && response.getChoices() != null
                && !response.getChoices().isEmpty()
                && response.getChoices().get(0).getMessage() != null) {
            return response.getChoices().get(0).getMessage().getContent();
        }
        return "暂未获取到视频建议，请稍后重试。";
    }

    @Override
    public AttentionIdiomInfo searchIdiom(String idiom) throws Exception {
        if (!StringUtils.hasText(idiom)) {
            throw new IllegalArgumentException("成语不能为空");
        }
        
        String idiomText = idiom.trim();
        
        // 先从数据库查询
        QueryWrapper<AttentionIdiomInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("idiom", idiomText);
        AttentionIdiomInfo existing = infoMapper.selectOne(queryWrapper);
        if (existing != null) {
            if (!StringUtils.hasText(existing.getVideoUrl())) {
                String videoUrl = findVideoUrlByIdiom(existing.getIdiom());
                if (StringUtils.hasText(videoUrl)) {
                    existing.setVideoUrl(videoUrl);
                    existing.setUpdateTime(new Date());
                    infoMapper.updateById(existing);
                }
            }
            return existing;
        }
        
        // 数据库中没有，调用DeepSeek获取
        String systemPrompt = "你是一名成语学习与记忆教练 + 专业的数字信息挖掘与分析专家。请为给定的成语提供完整资料，并仅返回JSON对象（禁止Markdown和解释）。必须字段：idiom, pinyin, literalMeaning, meaning, moral, originDynasty, originSource, background, story, protagonist, relatedPersons, usageExamples（数组，至少3条完整例句）, memoryCues, tags（3-8个，逗号分隔字符串）。流程要求：1) 资料完整输出。2) 严禁幻想，信息需基于真实可验证来源。仅输出单个JSON对象，不要额外文字。";
        
        String userPrompt = "请为成语【" + idiomText + "】提供完整的资料信息，包括至少3条使用示例。";
        
        List<DeepSeekMessage> messages = new ArrayList<>();
        messages.add(new DeepSeekMessage("system", systemPrompt));
        messages.add(new DeepSeekMessage("user", userPrompt));
        
        DeepSeekChatResponse response = deepSeekService.chat(messages);
        String content = response != null
                && response.getChoices() != null
                && !response.getChoices().isEmpty()
                && response.getChoices().get(0).getMessage() != null
                ? response.getChoices().get(0).getMessage().getContent()
                : "{}";
        
        String json = extractJsonObject(content);
        AttentionIdiomInfo idiomInfo = null;
        try {
            idiomInfo = JSON.parseObject(json, AttentionIdiomInfo.class);
            if (idiomInfo != null && StringUtils.hasText(idiomInfo.getIdiom())) {
                // 处理使用示例数组，转换为字符串存储
                if (json.contains("usageExamples")) {
                    try {
                        com.alibaba.fastjson.JSONObject jsonObj = JSON.parseObject(json);
                        Object usageExamplesObj = jsonObj.get("usageExamples");
                        if (usageExamplesObj != null) {
                            if (usageExamplesObj instanceof List) {
                                @SuppressWarnings("unchecked")
                                List<String> examples = (List<String>) usageExamplesObj;
                                idiomInfo.setUsageText(String.join("\n", examples));
                            } else if (usageExamplesObj instanceof String) {
                                idiomInfo.setUsageText((String) usageExamplesObj);
                            }
                        }
                    } catch (Exception e) {
                        // 忽略解析错误
                    }
                }
                
                idiomInfo.setIdiom(idiomInfo.getIdiom().trim());
                
                // 从 video.json 文件中查找对应的视频地址
                String videoUrl = findVideoUrlByIdiom(idiomInfo.getIdiom());
                if (StringUtils.hasText(videoUrl)) {
                    idiomInfo.setVideoUrl(videoUrl);
                }
                Date now = new Date();
                idiomInfo.setUpdateTime(now);
                idiomInfo.setCreateTime(now);
                
                // 保存到数据库
                infoMapper.insert(idiomInfo);
            }
        } catch (Exception e) {
            throw new RuntimeException("解析DeepSeek返回的成语信息失败：" + e.getMessage(), e);
        }
        
        if (idiomInfo == null || !StringUtils.hasText(idiomInfo.getIdiom())) {
            throw new RuntimeException("未能从DeepSeek获取到有效的成语信息");
        }
        
        return idiomInfo;
    }
    
    private String buildUserPrompt(IdiomGenerateRequest request, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append("请基于以下需求输出").append(count).append("条成语：");
        if (StringUtils.hasText(request.getPrompt())) {
            sb.append("主题/素材：").append(request.getPrompt()).append("；");
        }
        if (StringUtils.hasText(request.getDomain())) {
            sb.append("场景/领域：").append(request.getDomain()).append("；");
        }
        if (StringUtils.hasText(request.getDifficulty())) {
            sb.append("难度：").append(request.getDifficulty()).append("；");
        }
        if (!CollectionUtils.isEmpty(request.getTags())) {
            sb.append("标签：").append(String.join(",", request.getTags())).append("；");
        }
        sb.append("请确保JSON字段完整，tags使用逗号分隔字符串。");
        return sb.toString();
    }
    
    private String extractJsonArray(String content) {
        if (content == null) {
            return "[]";
        }
        String cleaned = content.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replace("```json", "").replace("```", "").trim();
        }
        int start = cleaned.indexOf('[');
        int end = cleaned.lastIndexOf(']');
        if (start >= 0 && end >= start) {
            return cleaned.substring(start, end + 1);
        }
        return cleaned;
    }
    
    private String extractJsonObject(String content) {
        if (content == null) {
            return "{}";
        }
        String cleaned = content.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replace("```json", "").replace("```", "").trim();
        }
        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');
        if (start >= 0 && end >= start) {
            return cleaned.substring(start, end + 1);
        }
        return cleaned;
    }
    
    /**
     * 从 video.json 文件中根据成语名称查找对应的视频地址
     * @param idiom 成语名称
     * @return 视频地址，如果未找到则返回 null
     */
    private String findVideoUrlByIdiom(String idiom) {
        if (!StringUtils.hasText(idiom)) {
            return null;
        }
        
        try {
            ClassPathResource resource = new ClassPathResource(ResourcesContstants.IDIOM_JSON);
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            inputStream.close();
            
            String jsonContent = sb.toString();
            JSONObject jsonObject = JSON.parseObject(jsonContent);
            JSONArray videos = jsonObject.getJSONArray("videos");
            
            if (videos != null && !videos.isEmpty()) {
                String idiomText = idiom.trim();
                for (int i = 0; i < videos.size(); i++) {
                    JSONObject video = videos.getJSONObject(i);
                    String title = video.getString("title");
                    if (StringUtils.hasText(title)) {
                        if ( title !=null && title.contains(idiomText)) {
                            return video.getString("url");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 如果读取文件失败，返回 null
            // 可以记录日志，但不影响主流程
        }
        
        return null;
    }
}


