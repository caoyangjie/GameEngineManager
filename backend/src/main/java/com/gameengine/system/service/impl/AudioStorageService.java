package com.gameengine.system.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 音频文件存储服务：支持 Base64 字符串或 URL 源，将音频保存到本地可配置目录
 * @author GameEngine
 */
@Slf4j
@Service
public class AudioStorageService {
    
    @Value("${gameengine.audio.save-path:${user.home}/gameengine/audio}")
    private String audioSavePath;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * 将音频内容（Base64 或 URL）保存到本地目录
     * 
     * @param audioContent 音频源（Base64 数据或可访问的 URL）
     * @param prefix 文件名前缀
     * @return 保存后的本地绝对路径
     */
    public String saveAudio(String audioContent, String prefix) throws Exception {
        if (audioContent == null || audioContent.trim().isEmpty()) {
            throw new IllegalArgumentException("音频内容不能为空");
        }
        
        Path targetDir = Paths.get(audioSavePath).toAbsolutePath().normalize();
        Files.createDirectories(targetDir);
        
        String safePrefix = (prefix == null || prefix.trim().isEmpty()) ? "audio" : prefix.trim();
        String filename = safePrefix + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "-" + UUID.randomUUID().toString().replace("-", "") + ".mp3";
        Path targetPath = targetDir.resolve(filename);
        
        byte[] audioBytes = audioContent.startsWith("http") ? downloadFromUrl(audioContent) : decodeBase64(audioContent);
        Files.write(targetPath, audioBytes);
        
        log.info("音频已保存至: {}", targetPath);
        return targetPath.toString();
    }
    
    private byte[] downloadFromUrl(String url) throws Exception {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("下载音频失败，URL: " + url);
        }
        return response.getBody();
    }
    
    private byte[] decodeBase64(String content) {
        String cleaned = content;
        int commaIndex = content.indexOf(',');
        if (commaIndex > 0) {
            // 去掉 data:audio/...;base64, 前缀
            cleaned = content.substring(commaIndex + 1);
        }
        return Base64.getDecoder().decode(cleaned);
    }
}

