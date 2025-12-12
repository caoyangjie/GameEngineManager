package com.gameengine.system.service;

import java.util.List;

import com.gameengine.system.domain.dto.DeepSeekChatRequest;
import com.gameengine.system.domain.dto.DeepSeekChatResponse;
import com.gameengine.system.domain.dto.DeepSeekMessage;

/**
 * DeepSeek AI 服务接口
 * 
 * @author GameEngine
 */
public interface IDeepSeekService {
    
    /**
     * 发送聊天请求
     * 
     * @param request 聊天请求
     * @return 聊天响应
     * @throws Exception 请求异常
     */
    DeepSeekChatResponse chat(DeepSeekChatRequest request) throws Exception;
    
    /**
     * 发送简单聊天消息
     * 
     * @param messages 消息列表
     * @return 聊天响应
     * @throws Exception 请求异常
     */
    DeepSeekChatResponse chat(List<DeepSeekMessage> messages) throws Exception;
    
    /**
     * 发送单条用户消息并获取回复
     * 
     * @param userMessage 用户消息
     * @param systemPrompt 系统提示词（可选）
     * @return AI 回复内容
     * @throws Exception 请求异常
     */
    String chatSimple(String userMessage, String systemPrompt) throws Exception;
    
    /**
     * 流式聊天（暂不支持，预留接口）
     * 
     * @param request 聊天请求
     * @return 流式响应
     * @throws Exception 请求异常
     */
    // Stream<DeepSeekChatResponse> chatStream(DeepSeekChatRequest request) throws Exception;
}

