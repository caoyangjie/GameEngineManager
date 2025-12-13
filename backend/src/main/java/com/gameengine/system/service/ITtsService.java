package com.gameengine.system.service;

/**
 * TTS 语音合成服务接口
 * 
 * @author GameEngine
 */
public interface ITtsService {
    
    /**
     * 合成语音并返回音频数据（Base64 编码的音频数据）
     * 
     * @param text 要合成的文本
     * @param voice 发音人，默认为 Cherry
     * @return Base64 编码的音频数据
     * @throws Exception 合成失败时抛出异常
     */
    String synthesize(String text, String voice) throws Exception;
}

