# TTS 语音合成配置说明

## 后端配置

在 `application.yml` 或 `application-local.yml` 中添加以下配置：

```yaml
# 阿里云百炼 TTS 配置
dashscope:
  tts:
    apiKey: sk-xxx  # 从阿里云百炼获取 API Key
```

## 获取 API Key

1. 访问 [阿里云百炼控制台](https://help.aliyun.com/zh/model-studio/get-api-key)
2. 登录后获取 API Key
3. 将 API Key 配置到 `application.yml` 中

**注意：**
- 北京地域和新加坡地域的 API Key 不同
- 北京地域 API 地址：`https://dashscope.aliyuncs.com`
- 新加坡地域 API 地址：`https://dashscope-intl.aliyuncs.com`

## API 接口

### POST /tts/synthesize

合成语音接口

**请求参数：**
- `text` (必填): 要合成的文本
- `voice` (可选): 发音人，默认为 `Cherry`

**响应：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": "Base64编码的音频数据"
}
```

## 发音人选项

阿里云百炼支持的发音人包括：
- `Cherry`: 樱桃（默认，女声）
- 更多发音人请参考阿里云百炼文档

## 依赖说明

后端使用 Spring Boot 自带的 `RestTemplate` 进行 HTTP 请求，无需额外依赖。

## 注意事项

1. 配置信息应保存在配置文件中，不要提交到版本控制系统
2. 如果未配置 TTS 参数，前端会自动降级为手动切换模式（无语音播报）
3. 音频格式由阿里云百炼 API 返回，通常为 MP3 或其他常见格式
4. 前端需要根据实际返回的音频格式进行播放处理

