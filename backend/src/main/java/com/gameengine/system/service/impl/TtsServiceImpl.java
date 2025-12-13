package com.gameengine.system.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gameengine.system.service.ITtsService;

import lombok.extern.slf4j.Slf4j;

/**
 * TTS 语音合成服务实现
 * 基于阿里云百炼 TTS API
 * 
 * @author GameEngine
 */
@Slf4j
@Service
public class TtsServiceImpl implements ITtsService {
    
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation";
    private static final String MODEL = "qwen3-tts-flash";
    
    @Value("${dashscope.tts.apiKey:}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    public TtsServiceImpl() {
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    public String synthesize(String text, String voice) throws Exception {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("文本不能为空");
        }
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("TTS 配置不完整，请检查 application.yml 中的 dashscope.tts.apiKey 配置");
        }
        
        // 如果 voice 为空，使用默认值
        if (voice == null || voice.trim().isEmpty()) {
            voice = "Cherry";
        }
        
        try {
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", MODEL);
            
            JSONObject input = new JSONObject();
            input.put("text", text);
            input.put("voice", voice);
            input.put("language_type", "Chinese");
            requestBody.put("input", input);
            
            String requestBodyStr = requestBody.toJSONString();
            log.debug("TTS 请求体: {}", requestBodyStr);
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            HttpEntity<String> entity = new HttpEntity<>(requestBodyStr, headers);
            
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                log.debug("TTS 响应: {}", responseBody);
                
                // 解析响应
                JSONObject responseJson = JSON.parseObject(responseBody);
                
                // 检查是否有错误
                if (responseJson.containsKey("code") && !responseJson.getString("code").equals("Success")) {
                    String message = responseJson.getString("message");
                    throw new Exception("TTS API 错误: " + message);
                }
                
                // 获取音频数据
                JSONObject output = responseJson.getJSONObject("output");
                if (output == null) {
                    throw new Exception("TTS API 响应格式错误：缺少 output 字段");
                }
                
                // 阿里云返回的音频数据可能是 base64 编码的字符串或 URL
                // 优先尝试获取 URL（output.audio.url）
                String audioUrl = null;
                String audioBase64 = null;
                
                // 尝试从 output.audio.url 获取音频 URL
                JSONObject audio = output.getJSONObject("audio");
                if (audio != null) {
                    audioUrl = audio.getString("url");
                    if (audioUrl != null && !audioUrl.trim().isEmpty()) {
                        log.info("获取到音频 URL: {}", audioUrl);
                        // 直接返回 URL，让前端直接使用 URL 播放
                        return audioUrl;
                    }
                    // 如果 audio 对象中有 data 字段且不为空，可能是 base64
                    String audioData = audio.getString("data");
                    if (audioData != null && !audioData.trim().isEmpty()) {
                        audioBase64 = audioData;
                    }
                }
                
                // 如果没有从 audio.url 获取到，尝试其他方式获取 base64
                if (audioBase64 == null || audioBase64.trim().isEmpty()) {
                    if (output.containsKey("audio") && output.get("audio") instanceof String) {
                        audioBase64 = output.getString("audio");
                    } else if (output.containsKey("audio_base64")) {
                        audioBase64 = output.getString("audio_base64");
                    } else if (output.containsKey("data")) {
                        Object data = output.get("data");
                        if (data instanceof String) {
                            audioBase64 = (String) data;
                        }
                    }
                }
                
                // 如果还是没有获取到，尝试从 output.audio_url 获取（旧格式兼容）
                if ((audioBase64 == null || audioBase64.trim().isEmpty()) && audioUrl == null) {
                    audioUrl = output.getString("audio_url");
                    if (audioUrl != null && !audioUrl.trim().isEmpty()) {
                        log.info("从 audio_url 获取到音频 URL: {}", audioUrl);
                        return audioUrl;
                    }
                }
                
                // 如果获取到了 base64，返回 base64
                if (audioBase64 != null && !audioBase64.trim().isEmpty()) {
                    return audioBase64;
                }
                
                // 如果都没有获取到，抛出异常
                throw new Exception("TTS API 响应格式错误：无法获取音频数据或 URL");
            } else {
                log.error("TTS API 请求失败，状态码: {}, 响应: {}", response.getStatusCode(), response.getBody());
                throw new Exception("TTS API 请求失败: " + response.getStatusCode());
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("TTS 合成失败", e);
            throw new Exception("TTS 合成失败: " + e.getMessage(), e);
        }
    }
}
