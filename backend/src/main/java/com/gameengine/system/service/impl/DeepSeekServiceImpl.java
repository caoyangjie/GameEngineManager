package com.gameengine.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.system.config.DeepSeekConfig;
import com.gameengine.system.domain.dto.DeepSeekChatRequest;
import com.gameengine.system.domain.dto.DeepSeekChatResponse;
import com.gameengine.system.domain.dto.DeepSeekMessage;
import com.gameengine.system.service.IDeepSeekService;

import lombok.extern.slf4j.Slf4j;

/**
 * DeepSeek AI 服务实现类
 * 
 * @author GameEngine
 */
@Slf4j
@Service
public class DeepSeekServiceImpl implements IDeepSeekService {
    
    private final DeepSeekConfig deepSeekConfig;
    private final RestTemplate restTemplate;
    
    public DeepSeekServiceImpl(DeepSeekConfig deepSeekConfig) {
        this.deepSeekConfig = deepSeekConfig;
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    public DeepSeekChatResponse chat(DeepSeekChatRequest request) throws Exception {
        // 验证配置
        if (deepSeekConfig.getApiKey() == null || deepSeekConfig.getApiKey().trim().isEmpty()) {
            throw new ServiceException("DeepSeek API Key 未配置，请在配置文件中设置 deepseek.api-key");
        }
        
        // 设置默认模型
        if (request.getModel() == null || request.getModel().trim().isEmpty()) {
            request.setModel(deepSeekConfig.getDefaultModel());
        }
        
        // 构建请求 URL
        String url = deepSeekConfig.getBaseUrl() + "/v1/chat/completions";
        
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(deepSeekConfig.getApiKey());
        
        // 构建请求体
        String requestBody = JSON.toJSONString(request);
        log.debug("DeepSeek 请求 URL: {}", url);
        log.debug("DeepSeek 请求体: {}", requestBody);
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                log.debug("DeepSeek 响应: {}", responseBody);
                
                DeepSeekChatResponse chatResponse = JSON.parseObject(responseBody, DeepSeekChatResponse.class);
                return chatResponse;
            } else {
                log.error("DeepSeek API 请求失败，状态码: {}, 响应: {}", response.getStatusCode(), response.getBody());
                throw new ServiceException("DeepSeek API 请求失败: " + response.getStatusCode());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("DeepSeek API 请求异常", e);
            throw new ServiceException("DeepSeek API 请求异常: " + e.getMessage());
        }
    }
    
    @Override
    public DeepSeekChatResponse chat(List<DeepSeekMessage> messages) throws Exception {
        DeepSeekChatRequest request = new DeepSeekChatRequest();
        request.setMessages(messages);
        return chat(request);
    }
    
    @Override
    public String chatSimple(String userMessage, String systemPrompt) throws Exception {
        List<DeepSeekMessage> messages = new ArrayList<>();
        
        // 添加系统提示词（如果有）
        if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
            messages.add(new DeepSeekMessage("system", systemPrompt));
        }
        
        // 添加用户消息
        messages.add(new DeepSeekMessage("user", userMessage));
        
        // 发送请求
        DeepSeekChatResponse response = chat(messages);
        
        // 提取回复内容
        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            DeepSeekMessage assistantMessage = response.getChoices().get(0).getMessage();
            if (assistantMessage != null && assistantMessage.getContent() != null) {
                return assistantMessage.getContent();
            }
        }
        
        throw new ServiceException("DeepSeek API 返回结果为空");
    }
}

