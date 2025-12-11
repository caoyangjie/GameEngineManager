package com.gameengine.system.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.system.domain.dto.SocialAuthLoginResult;
import com.gameengine.system.domain.dto.SocialAuthorizeInfo;
import com.gameengine.system.service.SocialAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.zhyd.oauth.model.AuthCallback;

/**
 * 第三方登录控制器
 *
 * @author GameEngine
 */
@RestController
@Tag(name = "第三方登录", description = "微信/QQ/飞书/钉钉登录")
public class SocialAuthController {
    private static final Logger logger = LoggerFactory.getLogger(SocialAuthController.class);

    private static final String MESSAGE_TYPE = "social-login";

    private final SocialAuthService socialAuthService;
    private final ObjectMapper objectMapper;

    public SocialAuthController(SocialAuthService socialAuthService, ObjectMapper objectMapper) {
        this.socialAuthService = socialAuthService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "获取第三方授权地址")
    @GetMapping("/oauth/authorize/{source}")
    public AjaxResult authorize(@PathVariable("source") String source) {
        SocialAuthorizeInfo info = socialAuthService.buildAuthorizeInfo(source);
        return AjaxResult.success(info);
    }

    @Operation(summary = "第三方登录回调")
    @GetMapping(value = "/oauth/callback/{source}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> callback(@PathVariable("source") String source, AuthCallback callback) {
        logger.info("第三方登录回调: source={}, callback={}", source, callback);
        Map<String, Object> payload = new HashMap<>(8);
        payload.put("type", MESSAGE_TYPE);
        try {
            SocialAuthLoginResult loginResult = socialAuthService.handleCallback(source, callback);
            payload.put("success", true);
            payload.put("token", loginResult.getToken());
            payload.put("source", loginResult.getSource());
            payload.put("userId", loginResult.getUserId());
            payload.put("userName", loginResult.getUserName());
            payload.put("nickName", loginResult.getNickName());
        } catch (ServiceException ex) {
            payload.put("success", false);
            payload.put("error", ex.getMessage());
        } catch (Exception ex) {
            payload.put("success", false);
            payload.put("error", "第三方登录失败: " + ex.getMessage());
        }
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(buildCallbackPage(payload));
    }

    private String buildCallbackPage(Map<String, Object> payload) {
        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            payloadJson = "{\"type\":\"" + MESSAGE_TYPE + "\",\"success\":false,\"error\":\"无法序列化登录结果\"}";
        }
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\">")
            .append("<title>登录结果</title></head><body>")
            .append("<script>(function(){")
            .append("var payload=").append(payloadJson).append(";")
            .append("if (window.opener) { try { window.opener.postMessage(payload, '*'); } catch(e) {} window.close(); }")
            .append("else { document.body.innerHTML='<pre>'+JSON.stringify(payload, null, 2)+'</pre>'; }")
            .append("})();</script>")
            .append("<noscript>登录结果已返回，请关闭此页面</noscript>")
            .append("</body></html>");
        return html.toString();
    }
}


