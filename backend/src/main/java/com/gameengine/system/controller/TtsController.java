package com.gameengine.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.service.ITtsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * TTS 语音合成控制器
 * 
 * @author GameEngine
 */
@Tag(name = "TTS 语音合成", description = "阿里云百炼 TTS 语音合成接口")
@RestController
@RequestMapping("/tts")
public class TtsController extends BaseController {
    
    @Autowired
    private ITtsService ttsService;
    
    @Operation(summary = "合成语音", description = "将文本合成为语音，返回 Base64 编码的音频数据")
    @PostMapping("/synthesize")
    public AjaxResult synthesize(@RequestBody JSONObject request) {
        try {
            String text = request.getString("text");
            String voice = request.getString("voice");
            
            if (text == null || text.trim().isEmpty()) {
                return error("文本不能为空");
            }
            
            if (voice == null || voice.trim().isEmpty()) {
                voice = "Cherry";
            }
            
            String audioBase64 = ttsService.synthesize(text, voice);
            return success(audioBase64);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        } catch (IllegalStateException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("TTS 合成失败", e);
            return error("TTS 合成失败: " + e.getMessage());
        }
    }
}

