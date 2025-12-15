package com.gameengine.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.AttentionNumberFaxRecord;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.service.IAttentionNumberFaxService;
import com.gameengine.system.service.ISysUserService;
import com.gameengine.system.service.ITtsService;
import com.gameengine.system.service.impl.AudioStorageService;

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
    
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Autowired
    private ITtsService ttsService;
    
    @Autowired
    private AudioStorageService audioStorageService;
    
    @Autowired
    private IAttentionNumberFaxService attentionNumberFaxService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private ISysUserService userService;
    
    @Operation(summary = "合成语音", description = "将文本合成为语音，返回 Base64 编码的音频数据")
    @PostMapping("/synthesize")
    public AjaxResult synthesize(@RequestBody JSONObject request, HttpServletRequest httpRequest) {
        try {
            String text = request.getString("text");
            String voice = request.getString("voice");
            
            if (text == null || text.trim().isEmpty()) {
                return error("文本不能为空");
            }
            
            if (voice == null || voice.trim().isEmpty()) {
                voice = "Cherry";
            }
            
            String audioContent = ttsService.synthesize(text, voice);
            
            // 保存音频到本地可配置目录
            // String savedPath = audioStorageService.saveAudio(audioContent, "tts");
            
            // 自动创建数字传真训练数据
            AttentionNumberFaxRecord savedRecord = createNumberFaxRecordIfPossible(request, httpRequest, audioContent);
            
            JSONObject result = new JSONObject();
            result.put("audio", audioContent);
            // result.put("filePath", savedPath);
            if (savedRecord != null) {
                result.put("recordId", savedRecord.getId());
            }
            
            return AjaxResult.success("TTS 合成成功", audioContent);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("TTS 合成失败", e);
            return error("TTS 合成失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据传入参数自动创建数字传真训练记录（仅当必填字段齐全时）
     */
    private AttentionNumberFaxRecord createNumberFaxRecordIfPossible(JSONObject request, HttpServletRequest httpRequest, String audioPath) {
        Integer targetNumber = request.getInteger("targetNumber");
        Integer groupCount = request.getInteger("groupCount");
        String groupsJson = request.getString("groupsJson");
        Integer correctAnswer = request.getInteger("correctAnswer");
        
        // 只有当核心字段齐全时才创建训练数据
        if (targetNumber == null || groupCount == null || groupCount <= 0
                || groupsJson == null || groupsJson.trim().isEmpty()
                || correctAnswer == null) {
            return null;
        }
        
        AttentionNumberFaxRecord faxRecord = new AttentionNumberFaxRecord();
        faxRecord.setTargetNumber(targetNumber);
        faxRecord.setGroupCount(groupCount);
        faxRecord.setGroupsJson(groupsJson);
        faxRecord.setCorrectAnswer(correctAnswer);
        faxRecord.setUserAnswer(request.getInteger("userAnswer"));
        faxRecord.setAudioUrl(audioPath);
        
        Long userId = getUserIdFromRequest(httpRequest);
        if (userId != null) {
            faxRecord.setUserId(userId);
            SysUser user = userService.selectUserById(userId);
            if (user != null) {
                String username = user.getNickName();
                if (username == null || username.trim().isEmpty()) {
                    username = user.getUserName();
                }
                faxRecord.setUsername(username != null ? username : "匿名用户");
            } else {
                faxRecord.setUsername("匿名用户");
            }
        } else {
            faxRecord.setUsername("匿名用户");
        }
        
        return attentionNumberFaxService.saveRecord(faxRecord);
    }
    
    /**
     * 从请求中解析用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }
        if (token == null) {
            return null;
        }
        return jwtUtils.getUserIdFromToken(token);
    }
}

