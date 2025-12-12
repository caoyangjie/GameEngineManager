package com.gameengine.system.init;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameengine.common.utils.ResourcesContstants;
import com.gameengine.system.domain.PoetryInfo;
import com.gameengine.system.mapper.PoetryInfoMapper;
import com.gameengine.system.service.IPoetryChallengeService;

/**
 * 应用启动时，根据 poem.json 初始化诗词及视频地址。
 */
@Component
public class PoetryVideoInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PoetryVideoInitializer.class);

    @Autowired
    private IPoetryChallengeService poetryChallengeService;

    @Autowired
    private PoetryInfoMapper poetryInfoMapper;

    @Override
    public void run(ApplicationArguments args) {
        initializeFromPoemJson();
    }

    private void initializeFromPoemJson() {
        List<VideoEntry> entries = loadVideoEntries();
        if (CollectionUtils.isEmpty(entries)) {
            log.warn("poem.json 中未找到视频数据，跳过诗词初始化");
            return;
        }

        Set<String> processed = new HashSet<>();
        CompletableFuture.allOf(entries.stream().map(entry -> CompletableFuture.runAsync(() -> {
            if (entry == null || !StringUtils.hasText(entry.getTitle()) || !processed.add(entry.getTitle())) {
                return;
            }

            String title = entry.getTitle().trim();
            PoetryInfo existing = poetryInfoMapper
                    .selectOne(new QueryWrapper<PoetryInfo>().eq("title", title));
            if (existing != null && StringUtils.hasText(existing.getVideoUrl())) {
                return;
            }

            try {
                PoetryInfo poetryInfo = poetryChallengeService.searchPoetry(title);
                if (poetryInfo != null && !StringUtils.hasText(poetryInfo.getVideoUrl())
                        && StringUtils.hasText(entry.getUrl())) {
                    poetryInfo.setVideoUrl(entry.getUrl());
                    poetryInfo.setUpdateTime(new Date());
                    if (poetryInfo.getId() != null) {
                        poetryInfoMapper.updateById(poetryInfo);
                    } else {
                        poetryInfoMapper.insert(poetryInfo);
                    }
                }
            } catch (Exception e) {
                log.warn("初始化诗词 [{}] 失败：{}", title, e.getMessage());
            }
        })).toArray(CompletableFuture[]::new)).join();
    }

    private List<VideoEntry> loadVideoEntries() {
        List<VideoEntry> results = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(ResourcesContstants.POEM_JSON);
        try (InputStream inputStream = resource.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObject = JSON.parseObject(sb.toString());
            JSONArray videos = jsonObject.getJSONArray("videos");
            if (videos == null) {
                return results;
            }
            for (int i = 0; i < videos.size(); i++) {
                JSONObject video = videos.getJSONObject(i);
                if (video == null) {
                    continue;
                }
                String videoTitle = video.getString("title");
                String url = video.getString("url");
                String title = extractPoetryTitle(videoTitle);
                if (StringUtils.hasText(title) && StringUtils.hasText(url)) {
                    results.add(new VideoEntry(title, url));
                }
            }
        } catch (Exception e) {
            log.error("读取 poem.json 失败：{}", e.getMessage(), e);
        }
        return results;
    }

    /**
     * 从视频标题中提取诗词标题
     * 支持多种格式：
     * - "001 咏鹅-骆宾王 （小学）" -> "咏鹅"
     * - "01.春晓" -> "春晓"
     * - "【爱上古诗】别董大———高适.0." -> "别董大"
     * - "【爱上古诗】登幽州台歌——陈子昂_高清 720P16." -> "登幽州台歌"
     * - "【爱上古诗】蜂(罗隐).22." -> "蜂"
     * - "191 送朱大入秦-孟浩然（直应为-值）" -> "送朱大入秦"
     */
    private String extractPoetryTitle(String videoTitle) {
        if (!StringUtils.hasText(videoTitle)) {
            return null;
        }
        String cleaned = videoTitle.trim();
        String original = cleaned; // 保存原始值用于日志
        
        // 1. 去掉【爱上古诗】等前缀
        cleaned = cleaned.replaceFirst("^【[^】]+】", "");
        
        // 2. 去掉开头的编号及分隔符（支持多种格式：001、01.、001 等）
        cleaned = cleaned.replaceFirst("^\\d+\\s*[\\.．、]\\s*", "");
        cleaned = cleaned.replaceFirst("^\\d+\\s+", "");
        
        // 3. 去掉末尾的各种后缀（_高清、720P、数字编号等）
        // 处理 "_高清 720P16." 格式：匹配 "_高清" + 可选空格 + "720P" + 数字 + 可选点号
        cleaned = cleaned.replaceFirst("_高清\\s*720P\\d+[\\.．]?\\s*$", "");
        // 处理 "_高清 .30." 格式：匹配 "_高清" + 空格 + 点号 + 数字 + 可选点号
        cleaned = cleaned.replaceFirst("_高清\\s*[\\.．]\\d+[\\.．]?\\s*$", "");
        // 处理单独的 "_高清" 后缀
        cleaned = cleaned.replaceFirst("_高清\\s*$", "");
        // 处理末尾的编号（如 .0.、.1.、.76.、.30. 等）
        cleaned = cleaned.replaceFirst("[\\.．]\\d+[\\.．]?\\s*$", "");
        
        // 4. 去掉括号内容（包括作者在括号中的情况，如"蜂(罗隐)"）
        cleaned = cleaned.replaceAll("[（(][^）)]+[）)]", "");
        
        // 5. 去掉作者信息（格式：-作者名 或 ——作者名 或 ———作者名）
        // 匹配一个或多个短横线（-、—），后跟作者名（通常是中文字符），直到结尾
        // 作者名通常以中文字符开头，可能包含一些标点符号
        cleaned = cleaned.replaceFirst("[-—]+\\s*[\\u4e00-\\u9fa5]+[\\u4e00-\\u9fa5\\s\\-—]*\\s*$", "");
        
        // 6. 清理多余的空格和可能的残留短横线、下划线等
        cleaned = cleaned.replaceFirst("[-—]+\\s*$", "");
        cleaned = cleaned.replaceFirst("_+\\s*$", "");
        cleaned = cleaned.trim();
        
        // 如果提取结果为空或只包含符号，记录警告
        if (!StringUtils.hasText(cleaned) || cleaned.matches("^[-—\\s_]+$")) {
            log.warn("无法从视频标题中提取诗词名：{}", original);
            return null;
        }
        
        return cleaned;
    }

    private static class VideoEntry {
        private final String title;
        private final String url;

        VideoEntry(String title, String url) {
            this.title = title;
            this.url = url;
        }

        String getTitle() {
            return title;
        }

        String getUrl() {
            return url;
        }
    }
}

