package com.gameengine.system.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import com.gameengine.common.utils.ResourcesContstants;
import com.gameengine.system.domain.PoetryChallengeRecord;
import com.gameengine.system.domain.PoetryInfo;
import com.gameengine.system.domain.dto.DeepSeekChatResponse;
import com.gameengine.system.domain.dto.DeepSeekMessage;
import com.gameengine.system.domain.dto.PoetryGenerateRequest;
import com.gameengine.system.mapper.PoetryChallengeRecordMapper;
import com.gameengine.system.mapper.PoetryInfoMapper;
import com.gameengine.system.service.IDeepSeekService;
import com.gameengine.system.service.IPoetryChallengeService;

/**
 * 诗词挑战记忆服务实现
 */
@Service
public class PoetryChallengeServiceImpl implements IPoetryChallengeService {
    
    private static final Logger log = LoggerFactory.getLogger(PoetryChallengeServiceImpl.class);
    
    private static final String SYSTEM_PROMPT = "你是一名诗词学习与记忆教练。请按照用户提供的主题/难度生成可用于记忆训练的诗词列表，并\"仅\"返回JSON数组，禁止输出任何Markdown或解释。每个对象需要包含字段：title（诗词标题）, author（作者）, dynasty（朝代）, content（诗词全文）, pinyin（拼音标注，可选）, poetryType（诗词类型，如五言绝句、七言律诗等）, meaning（释义/译文）, background（创作背景）, appreciation（诗词赏析）, theme（主题/情感）, keywords（关键词/意象）, memoryCues（记忆线索）, usageText（使用场景/例句，至少3条，用换行符分隔）, tags（逗号分隔标签，3-8个）。要求：1) 诗词为经典名篇且与主题贴合；2) background与appreciation简明扼要，突出记忆点；3) usageText 给出至少3条现代使用提示或例句；4) memoryCues 给出联想或画面线索。直接返回JSON数组即可。";
    
    @Autowired
    private PoetryChallengeRecordMapper recordMapper;
    
    @Autowired
    private PoetryInfoMapper infoMapper;
    
    @Autowired
    private IDeepSeekService deepSeekService;
    
    @Override
    public PoetryChallengeRecord saveRecord(PoetryChallengeRecord record) {
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
    public List<PoetryChallengeRecord> getTopRecords(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        QueryWrapper<PoetryChallengeRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("accuracy").orderByDesc("correct_count").last("LIMIT " + limit);
        return recordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<PoetryInfo> generatePoetries(PoetryGenerateRequest request, Long userId) throws Exception {
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
        List<PoetryInfo> poetries = new ArrayList<>();
        try {
            List<PoetryInfo> parsed = JSON.parseArray(json, PoetryInfo.class);
            if (!CollectionUtils.isEmpty(parsed)) {
                poetries = parsed;
            }
        } catch (Exception e) {
            // fallback to empty list
        }
        
        Date now = new Date();
        List<PoetryInfo> limitedList = poetries.stream().filter(Objects::nonNull).limit(count).collect(Collectors.toList());
        for (PoetryInfo poetry : limitedList) {
            if (poetry.getTitle() != null) {
                poetry.setTitle(poetry.getTitle().trim());
            }
            poetry.setCreatedBy(userId);
            poetry.setUpdateTime(now);
            if (poetry.getCreateTime() == null) {
                poetry.setCreateTime(now);
            }
            
            // upsert by title and author
            if (StringUtils.hasText(poetry.getTitle())) {
                QueryWrapper<PoetryInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("title", poetry.getTitle());
                if (StringUtils.hasText(poetry.getAuthor())) {
                    queryWrapper.eq("author", poetry.getAuthor());
                }
                PoetryInfo existing = infoMapper.selectOne(queryWrapper);
                if (existing != null) {
                    poetry.setId(existing.getId());
                    poetry.setCreateTime(existing.getCreateTime());
                    infoMapper.updateById(poetry);
                } else {
                    try {
                        infoMapper.insert(poetry);
                    } catch (DataIntegrityViolationException e) {
                        // 处理重复键错误
                        if (isDuplicateEntryError(e)) {
                            log.warn("检测到重复键错误，诗词标题：{}，将查询已存在的记录", poetry.getTitle());
                            // 重新查询已存在的记录
                            PoetryInfo duplicate = infoMapper.selectOne(queryWrapper);
                            if (duplicate != null) {
                                poetry.setId(duplicate.getId());
                                poetry.setCreateTime(duplicate.getCreateTime());
                                infoMapper.updateById(poetry);
                            } else {
                                log.error("重复键错误但查询不到已存在的记录，标题：{}", poetry.getTitle());
                            }
                        } else {
                            throw e;
                        }
                    }
                }
            }
        }
        
        return limitedList;
    }
    
    @Override
    public List<PoetryInfo> getLatestPoetries(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 8;
        }
        QueryWrapper<PoetryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time").last("LIMIT " + limit);
        return infoMapper.selectList(queryWrapper);
    }

    @Override
    public List<PoetryInfo> getPoetriesByTag(String tag, Integer limit) {
        if (!StringUtils.hasText(tag)) {
            return new ArrayList<>();
        }
        if (limit == null || limit <= 0) {
            limit = 30;
        }
        QueryWrapper<PoetryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("tags", tag).orderByDesc("update_time").last("LIMIT " + limit);
        return infoMapper.selectList(queryWrapper);
    }

    @Override
    public List<String> getAllTags() {
        List<PoetryInfo> all = infoMapper.selectList(new QueryWrapper<>());
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
    public String getVideoSuggestion(String title) throws Exception {
        if (!StringUtils.hasText(title)) {
            return "未提供诗词标题，无法生成视频建议";
        }

        String systemPrompt = "你是一名诗词讲解视频策划助手，擅长给出视频讲解的结构和搜索关键词。输出简短、可执行的建议。";
        String userPrompt = "请为诗词【" + title + "】提供1-2个讲解视频的创意方向，" +
                "给出可搜索的中文标题/关键词，以及30秒以内的讲解提纲（突出创作背景、诗词赏析、记忆法）。" +
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
    public PoetryInfo searchPoetry(String title) throws Exception {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("诗词标题不能为空");
        }
        
        String titleText = title.trim();
        
        // 先从数据库查询
        QueryWrapper<PoetryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", titleText);
        PoetryInfo existing = infoMapper.selectOne(queryWrapper);
        if (existing != null) {
            if (!StringUtils.hasText(existing.getVideoUrl())) {
                String videoUrl = findVideoUrlByTitle(existing.getTitle());
                if (StringUtils.hasText(videoUrl)) {
                    existing.setVideoUrl(videoUrl);
                    existing.setUpdateTime(new Date());
                    infoMapper.updateById(existing);
                }
            }
            return existing;
        }
        
        // 数据库中没有，调用DeepSeek获取
        String systemPrompt = "你是一名诗词学习与记忆教练 + 专业的数字信息挖掘与分析专家。请为给定的诗词提供完整资料，并仅返回JSON对象（禁止Markdown和解释）。必须字段：title, author, dynasty, content（诗词全文）, pinyin（可选）, poetryType, meaning, background, appreciation, theme, keywords, usageText（至少3条完整例句，用换行符分隔）, memoryCues, tags（3-8个，逗号分隔字符串）。流程要求：1) 资料完整输出。2) 严禁幻想，信息需基于真实可验证来源。仅输出单个JSON对象，不要额外文字。";
        
        String userPrompt = "请为诗词【" + titleText + "】提供完整的资料信息，包括至少3条使用示例。";
        
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
        PoetryInfo poetryInfo = null;
        try {
            poetryInfo = JSON.parseObject(json, PoetryInfo.class);
            if (poetryInfo != null && StringUtils.hasText(poetryInfo.getTitle())) {
                poetryInfo.setTitle(titleText);
                
                // 从 poem.json 文件中查找对应的视频地址
                String videoUrl = findVideoUrlByTitle(poetryInfo.getTitle());
                if (StringUtils.hasText(videoUrl)) {
                    poetryInfo.setVideoUrl(videoUrl);
                }
                Date now = new Date();
                poetryInfo.setUpdateTime(now);
                poetryInfo.setCreateTime(now);
                
                // 保存到数据库前，再次检查是否存在（避免并发情况下的重复插入）
                QueryWrapper<PoetryInfo> checkWrapper = new QueryWrapper<>();
                checkWrapper.eq("title", poetryInfo.getTitle());
                PoetryInfo checkExisting = infoMapper.selectOne(checkWrapper);
                if (checkExisting != null) {
                    // 已存在，直接返回
                    log.info("诗词【{}】已存在于数据库，跳过插入，直接返回", poetryInfo.getTitle());
                    return checkExisting;
                }
                
                // 保存到数据库
                try {
                    infoMapper.insert(poetryInfo);
                } catch (DataIntegrityViolationException e) {
                    // 处理重复键错误
                    if (isDuplicateEntryError(e)) {
                        log.warn("检测到重复键错误，诗词标题：{}，将查询已存在的记录并返回", poetryInfo.getTitle());
                        // 查询已存在的记录并返回
                        PoetryInfo duplicate = infoMapper.selectOne(checkWrapper);
                        if (duplicate != null) {
                            return duplicate;
                        } else {
                            log.error("重复键错误但查询不到已存在的记录，标题：{}", poetryInfo.getTitle());
                            throw new RuntimeException("检测到重复键错误，但查询不到已存在的记录，标题：" + poetryInfo.getTitle());
                        }
                    } else {
                        throw e;
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("解析DeepSeek返回的诗词信息失败：" + e.getMessage(), e);
        }
        
        if (poetryInfo == null || !StringUtils.hasText(poetryInfo.getTitle())) {
            throw new RuntimeException("未能从DeepSeek获取到有效的诗词信息");
        }
        
        return poetryInfo;
    }
    
    private String buildUserPrompt(PoetryGenerateRequest request, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append("请基于以下需求输出").append(count).append("首诗词：");
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
        sb.append("请确保JSON字段完整，tags使用逗号分隔字符串，usageText使用换行符分隔多条例句。");
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
     * 从 poem.json 文件中根据诗词标题查找对应的视频地址
     * @param title 诗词标题
     * @return 视频地址，如果未找到则返回 null
     */
    private String findVideoUrlByTitle(String title) {
        if (!StringUtils.hasText(title)) {
            return null;
        }
        
        try {
            ClassPathResource resource = new ClassPathResource(ResourcesContstants.POEM_JSON);
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
                String titleText = title.trim();
                for (int i = 0; i < videos.size(); i++) {
                    JSONObject video = videos.getJSONObject(i);
                    String videoTitle = video.getString("title");
                    if (StringUtils.hasText(videoTitle)) {
                        // 提取诗词标题（去掉编号和作者信息）
                        String extractedTitle = extractPoetryTitle(videoTitle);
                        if (extractedTitle != null && extractedTitle.contains(titleText)) {
                            return video.getString("url");
                        }
                        // 也尝试直接匹配
                        if (videoTitle.contains(titleText)) {
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
    
    /**
     * 从视频标题中提取诗词标题
     * 例如："001 咏鹅-骆宾王 （小学）" -> "咏鹅"
     */
    private String extractPoetryTitle(String videoTitle) {
        if (!StringUtils.hasText(videoTitle)) {
            return null;
        }
        String cleaned = videoTitle.trim();
        // 去掉编号及分隔符
        cleaned = cleaned.replaceFirst("^\\d+\\s*[\\.．、]\\s*", "");
        cleaned = cleaned.replaceFirst("^\\d+\\s*", "");
        // 去掉作者信息（格式：-作者名）
        cleaned = cleaned.replaceFirst("\\s*-\\s*[^（(]+", "");
        // 去掉括号内容
        cleaned = cleaned.replaceAll("[（(][^）)]+[）)]", "").trim();
        return cleaned;
    }
    
    /**
     * 判断异常是否为重复键错误（Duplicate entry）
     * @param e 异常对象
     * @return 如果是重复键错误返回 true，否则返回 false
     */
    private boolean isDuplicateEntryError(Throwable e) {
        if (e == null) {
            return false;
        }
        
        String message = e.getMessage();
        if (message != null) {
            String lowerMessage = message.toLowerCase();
            // MySQL 重复键错误通常包含 "Duplicate entry" 或错误码 1062
            if (lowerMessage.contains("duplicate entry") || 
                lowerMessage.contains("1062") ||
                lowerMessage.contains("重复键") ||
                lowerMessage.contains("唯一约束")) {
                return true;
            }
        }
        
        // 检查 SQLException
        Throwable cause = e.getCause();
        if (cause instanceof SQLException) {
            SQLException sqlEx = (SQLException) cause;
            int errorCode = sqlEx.getErrorCode();
            // MySQL 错误码 1062 表示重复键错误
            if (errorCode == 1062) {
                return true;
            }
            String sqlState = sqlEx.getSQLState();
            // SQLState "23000" 表示完整性约束违反
            if ("23000".equals(sqlState) && sqlEx.getMessage() != null 
                && sqlEx.getMessage().toLowerCase().contains("duplicate")) {
                return true;
            }
        }
        
        // 递归检查嵌套异常
        if (cause != null && cause != e) {
            return isDuplicateEntryError(cause);
        }
        
        return false;
    }
}

