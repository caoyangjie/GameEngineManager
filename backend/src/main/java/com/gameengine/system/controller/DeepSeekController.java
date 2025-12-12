package com.gameengine.system.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.dto.DeepSeekChatRequest;
import com.gameengine.system.domain.dto.DeepSeekChatResponse;
import com.gameengine.system.domain.dto.DeepSeekMessage;
import com.gameengine.system.service.IDeepSeekService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * DeepSeek AI 控制器
 * 
 * @author GameEngine
 */
@Slf4j
@Tag(name = "DeepSeek AI", description = "DeepSeek AI 模型接口")
@RestController
@RequestMapping("/ai/deepseek")
public class DeepSeekController extends BaseController {
    
    private final IDeepSeekService deepSeekService;
    
    public DeepSeekController(IDeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }
    
    /**
     * 发送聊天请求
     * 
     * @param request 聊天请求
     * @return 聊天响应
     */
    @Operation(summary = "发送聊天请求", description = "向 DeepSeek AI 发送聊天请求并获取回复")
    @PostMapping("/chat")
    public AjaxResult chat(@Valid @RequestBody DeepSeekChatRequest request) {
        try {
            DeepSeekChatResponse response = deepSeekService.chat(request);
            return success(response);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            log.error("DeepSeek 聊天请求异常", e);
            return error("请求失败: " + e.getMessage());
        }
    }
    
    /**
     * 发送简单聊天消息
     * 
     * @param messages 消息列表
     * @return 聊天响应
     */
    @Operation(summary = "发送简单聊天消息", description = "使用消息列表发送聊天请求")
    @PostMapping("/chat/messages")
    public AjaxResult chatWithMessages(@RequestBody List<DeepSeekMessage> messages) {
        try {
            if (messages == null || messages.isEmpty()) {
                return error("消息列表不能为空");
            }
            DeepSeekChatResponse response = deepSeekService.chat(messages);
            return success(response);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            log.error("DeepSeek 聊天请求异常", e);
            return error("请求失败: " + e.getMessage());
        }
    }
    
    /**
     * 发送单条用户消息并获取回复
     * 
     * @param userMessage 用户消息
     * @param systemPrompt 系统提示词（可选）
     * @return AI 回复内容
     */
    @Operation(summary = "简单聊天", description = "发送单条用户消息并获取 AI 回复")
    @PostMapping("/chat/simple")
    public AjaxResult chatSimple(
            @RequestParam String userMessage,
            @RequestParam(required = false) String systemPrompt) {
        try {
            if (userMessage == null || userMessage.trim().isEmpty()) {
                return error("用户消息不能为空");
            }
            String response = deepSeekService.chatSimple(userMessage, systemPrompt);
            return success(response);
        } catch (ServiceException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            log.error("DeepSeek 简单聊天请求异常", e);
            return error("请求失败: " + e.getMessage());
        }
    }
}

