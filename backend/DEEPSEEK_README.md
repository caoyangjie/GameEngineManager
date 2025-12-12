# DeepSeek AI 组件使用文档

## 概述

本组件提供了一套完整的 DeepSeek AI 模型对接功能，包括后端服务和前端 API 封装，方便在项目中快速集成和使用 DeepSeek AI 能力。

## 功能特性

- ✅ 完整的聊天对话功能
- ✅ 支持系统提示词设置
- ✅ 支持自定义模型参数（温度、Top-p、最大 Token 数等）
- ✅ 前后端分离架构
- ✅ RESTful API 设计
- ✅ Swagger API 文档支持
- ✅ 错误处理和日志记录

## 配置说明

### 后端配置

在 `application.yml` 中添加以下配置：

```yaml
# DeepSeek AI 配置
deepseek:
  # API Key（必填，从环境变量或配置文件中获取）
  api-key: ${DEEPSEEK_API_KEY:}
  # API 基础 URL（可选，默认使用官方地址）
  base-url: https://api.deepseek.com
  # 默认模型（可选，默认为 deepseek-chat）
  default-model: deepseek-chat
  # 请求超时时间（毫秒，可选，默认 60000）
  timeout: 60000
  # 最大重试次数（可选，默认 3）
  max-retries: 3
```

**重要提示：** 
- `api-key` 是必填项，建议通过环境变量 `DEEPSEEK_API_KEY` 设置，避免将密钥直接写在配置文件中
- 可以在 DeepSeek 官网（https://platform.deepseek.com/）申请 API Key

### 环境变量配置

推荐使用环境变量方式配置 API Key：

```bash
export DEEPSEEK_API_KEY=your_api_key_here
```

或者在启动时指定：

```bash
java -jar app.jar --deepseek.api-key=your_api_key_here
```

## API 接口说明

### 1. 发送聊天请求

**接口地址：** `POST /ai/deepseek/chat`

**请求体：**

```json
{
  "model": "deepseek-chat",
  "messages": [
    {
      "role": "system",
      "content": "你是一个专业的助手"
    },
    {
      "role": "user",
      "content": "你好，请介绍一下你自己"
    }
  ],
  "temperature": 1.0,
  "topP": 1.0,
  "maxTokens": 4096,
  "stream": false
}
```

**响应示例：**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": "chatcmpl-xxx",
    "object": "chat.completion",
    "created": 1234567890,
    "model": "deepseek-chat",
    "choices": [
      {
        "index": 0,
        "message": {
          "role": "assistant",
          "content": "你好！我是 DeepSeek AI 助手..."
        },
        "finishReason": "stop"
      }
    ],
    "usage": {
      "promptTokens": 20,
      "completionTokens": 50,
      "totalTokens": 70
    }
  }
}
```

### 2. 使用消息列表发送聊天请求

**接口地址：** `POST /ai/deepseek/chat/messages`

**请求体：**

```json
[
  {
    "role": "system",
    "content": "你是一个专业的助手"
  },
  {
    "role": "user",
    "content": "你好"
  }
]
```

### 3. 简单聊天接口

**接口地址：** `POST /ai/deepseek/chat/simple`

**请求参数：**
- `userMessage` (必填): 用户消息
- `systemPrompt` (可选): 系统提示词

**示例：**

```
POST /ai/deepseek/chat/simple?userMessage=你好&systemPrompt=你是一个专业的助手
```

## 后端使用示例

### Service 层使用

```java
@Autowired
private IDeepSeekService deepSeekService;

// 方式1：使用完整请求对象
DeepSeekChatRequest request = new DeepSeekChatRequest();
request.setModel("deepseek-chat");
List<DeepSeekMessage> messages = new ArrayList<>();
messages.add(new DeepSeekMessage("user", "你好"));
request.setMessages(messages);
DeepSeekChatResponse response = deepSeekService.chat(request);

// 方式2：使用消息列表
List<DeepSeekMessage> messages = new ArrayList<>();
messages.add(new DeepSeekMessage("system", "你是一个专业的助手"));
messages.add(new DeepSeekMessage("user", "你好"));
DeepSeekChatResponse response = deepSeekService.chat(messages);

// 方式3：简单聊天
String reply = deepSeekService.chatSimple("你好", "你是一个专业的助手");
```

## 前端使用示例

### 1. 使用 API 封装

```javascript
import { chat, chatSimple, chatWithMessages, createUserMessage, createSystemMessage } from '@/api/deepseek.js'

// 方式1：完整请求
const response = await chat({
  model: 'deepseek-chat',
  messages: [
    createSystemMessage('你是一个专业的助手'),
    createUserMessage('你好')
  ],
  temperature: 1.0,
  maxTokens: 4096
})

// 方式2：使用消息列表
const messages = [
  createSystemMessage('你是一个专业的助手'),
  createUserMessage('你好')
]
const response = await chatWithMessages(messages)

// 方式3：简单聊天
const reply = await chatSimple('你好', '你是一个专业的助手')
```

### 2. 使用聊天组件

```vue
<template>
  <DeepSeekChat
    :initial-system-prompt="'你是一个专业的助手'"
    :show-system-prompt-input="true"
  />
</template>

<script>
import DeepSeekChat from '@/components/common/DeepSeekChat.vue'

export default {
  components: {
    DeepSeekChat
  }
}
</script>
```

## 项目结构

### 后端文件结构

```
GameEngineManager/backend/src/main/java/com/gameengine/system/
├── config/
│   └── DeepSeekConfig.java              # 配置类
├── controller/
│   └── DeepSeekController.java          # 控制器
├── domain/
│   └── dto/
│       ├── DeepSeekMessage.java         # 消息 DTO
│       ├── DeepSeekChatRequest.java     # 请求 DTO
│       └── DeepSeekChatResponse.java    # 响应 DTO
└── service/
    ├── IDeepSeekService.java            # 服务接口
    └── impl/
        └── DeepSeekServiceImpl.java     # 服务实现
```

### 前端文件结构

```
GameEngineWeb/src/
├── api/
│   └── deepseek.js                      # API 封装
└── components/
    └── common/
        └── DeepSeekChat.vue             # 聊天组件
```

## 开发新功能

基于这套 DeepSeek AI 组件，您可以轻松开发新的功能，例如：

1. **内容生成功能**
   - 文章生成
   - 代码生成
   - 翻译功能

2. **智能问答功能**
   - 知识问答
   - 文档问答
   - 客服机器人

3. **数据分析功能**
   - 文本分析
   - 情感分析
   - 摘要生成

### 示例：开发内容生成功能

```java
@Service
public class ContentGenerationService {
    
    @Autowired
    private IDeepSeekService deepSeekService;
    
    /**
     * 生成文章
     */
    public String generateArticle(String topic, String style) throws Exception {
        String systemPrompt = String.format(
            "你是一个专业的文章写作助手。请根据用户提供的主题和风格要求，生成一篇高质量的文章。风格：%s", 
            style
        );
        String userMessage = String.format("请写一篇关于《%s》的文章", topic);
        return deepSeekService.chatSimple(userMessage, systemPrompt);
    }
}
```

## 注意事项

1. **API Key 安全**
   - 不要将 API Key 提交到代码仓库
   - 使用环境变量或配置中心管理密钥
   - 定期轮换 API Key

2. **请求限制**
   - 注意 DeepSeek API 的请求频率限制
   - 合理设置超时时间和重试次数
   - 监控 API 使用量和费用

3. **错误处理**
   - 所有 API 调用都应该包含错误处理
   - 记录详细的错误日志便于排查问题
   - 对用户友好的错误提示

4. **性能优化**
   - 对于频繁调用的场景，考虑添加缓存
   - 合理设置 `maxTokens` 参数控制响应长度
   - 使用流式输出（stream）提升用户体验（待实现）

## 常见问题

**Q: 如何获取 DeepSeek API Key？**  
A: 访问 https://platform.deepseek.com/ 注册账号并申请 API Key。

**Q: 支持流式输出吗？**  
A: 当前版本暂不支持流式输出，但接口已预留，后续版本会添加。

**Q: 如何自定义模型参数？**  
A: 在调用 `chat()` 方法时，设置 `DeepSeekChatRequest` 对象的相应参数即可。

**Q: 前端组件如何自定义样式？**  
A: 可以通过 CSS 覆盖组件样式，或者直接使用 API 封装自行开发 UI。

## 更新日志

- **v1.0.0** (2024-XX-XX)
  - 初始版本
  - 支持基础聊天功能
  - 提供前后端完整组件

