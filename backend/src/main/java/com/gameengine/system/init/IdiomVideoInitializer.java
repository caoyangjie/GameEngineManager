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
import com.gameengine.system.domain.AttentionIdiomInfo;
import com.gameengine.system.mapper.AttentionIdiomInfoMapper;
import com.gameengine.system.service.IAttentionIdiomAdvancedService;

/**
 * 应用启动时，根据 video.json 初始化成语及视频地址。
 */
@Component
public class IdiomVideoInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(IdiomVideoInitializer.class);

    @Autowired
    private IAttentionIdiomAdvancedService attentionIdiomAdvancedService;

    @Autowired
    private AttentionIdiomInfoMapper attentionIdiomInfoMapper;

    @Override
    public void run(ApplicationArguments args) {
        initializeFromVideoJson();
    }

    private void initializeFromVideoJson() {
        List<VideoEntry> entries = loadVideoEntries();
        if (CollectionUtils.isEmpty(entries)) {
            log.warn("video.json 中未找到视频数据，跳过成语初始化");
            return;
        }

        Set<String> processed = new HashSet<>();
        for (VideoEntry entry : entries) {
            if (entry == null || !StringUtils.hasText(entry.getIdiom()) || !processed.add(entry.getIdiom())) {
                continue;
            }

            String idiom = entry.getIdiom().trim();
            AttentionIdiomInfo existing = attentionIdiomInfoMapper
                    .selectOne(new QueryWrapper<AttentionIdiomInfo>().eq("idiom", idiom));
            if (existing != null && StringUtils.hasText(existing.getVideoUrl())) {
                continue;
            }

            try {
                AttentionIdiomInfo idiomInfo = attentionIdiomAdvancedService.searchIdiom(idiom);
                if (idiomInfo != null && !StringUtils.hasText(idiomInfo.getVideoUrl())
                        && StringUtils.hasText(entry.getUrl())) {
                    idiomInfo.setVideoUrl(entry.getUrl());
                    idiomInfo.setUpdateTime(new Date());
                    if (idiomInfo.getId() != null) {
                        attentionIdiomInfoMapper.updateById(idiomInfo);
                    } else {
                        attentionIdiomInfoMapper.insert(idiomInfo);
                    }
                }
            } catch (Exception e) {
                log.warn("初始化成语 [{}] 失败：{}", idiom, e.getMessage());
            }
        }
    }

    private List<VideoEntry> loadVideoEntries() {
        List<VideoEntry> results = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("video.json");
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
                String title = video.getString("title");
                String url = video.getString("url");
                String idiom = extractIdiom(title);
                if (StringUtils.hasText(idiom) && StringUtils.hasText(url)) {
                    results.add(new VideoEntry(idiom, url));
                }
            }
        } catch (Exception e) {
            log.error("读取 video.json 失败：{}", e.getMessage(), e);
        }
        return results;
    }

    private String extractIdiom(String title) {
        if (!StringUtils.hasText(title)) {
            return null;
        }
        String cleaned = title.trim();
        // 去掉编号及分隔符
        cleaned = cleaned.replaceFirst("^\\d+\\s*[\\.．、]\\s*", "");
        cleaned = cleaned.replaceFirst("^\\d+\\s*", "");
        return cleaned.trim();
    }

    private static class VideoEntry {
        private final String idiom;
        private final String url;

        VideoEntry(String idiom, String url) {
            this.idiom = idiom;
            this.url = url;
        }

        String getIdiom() {
            return idiom;
        }

        String getUrl() {
            return url;
        }
    }
}

