package com.gameengine.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.framework.web.controller.BaseController;
import com.gameengine.system.domain.WritingTrainingErrorLog;
import com.gameengine.system.domain.WritingTrainingQuestion;
import com.gameengine.system.domain.WritingTrainingRecord;
import com.gameengine.system.mapper.WritingTrainingErrorLogMapper;
import com.gameengine.system.service.IWritingTrainingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 写作训练题目与记录接口
 */
@Slf4j
@Tag(name = "写作训练", description = "AI 生成写作题目、训练记录管理")
@RestController
@RequestMapping("/writing-training")
public class WritingTrainingController extends BaseController {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private IWritingTrainingService writingTrainingService;

    @Autowired
    private WritingTrainingErrorLogMapper errorLogMapper;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 批量保存 AI 生成的题目
     *
     * 请求体（兼容两种格式）：
     * 1）旧格式：{ moduleKey, moduleTitle, difficulty, questions: string[] }
     * 2）新格式：{
     *      moduleKey,
     *      moduleTitle,
     *      difficulty,
     *      questions: string[],
     *      items: [{ content: '题目', samples: ['示例答案1', ...] }]
     *    }
     */
    @Operation(summary = "批量保存写作训练题目")
    @PostMapping("/questions/batch")
    public AjaxResult saveQuestions(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("用户未登录");
            }
            String moduleKey = (String) body.get("moduleKey");
            String moduleTitle = (String) body.get("moduleTitle");
            String difficulty = (String) body.get("difficulty");
            @SuppressWarnings("unchecked")
            List<String> questions = (List<String>) body.get("questions");

            // 新格式：items 数组，包含题目与示例答案
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");

            List<String> normalizedQuestions = new ArrayList<>();
            List<List<String>> sampleAnswers = new ArrayList<>();

            if (items != null && !items.isEmpty()) {
                for (Map<String, Object> item : items) {
                    if (item == null) {
                        continue;
                    }
                    Object contentObj = item.get("content");
                    String content = contentObj != null ? contentObj.toString().trim() : null;
                    if (content == null || content.isEmpty()) {
                        continue;
                    }
                    normalizedQuestions.add(content);

                    @SuppressWarnings("unchecked")
                    List<Object> samplesObj = (List<Object>) item.get("samples");
                    List<String> oneSamples = new ArrayList<>();
                    if (samplesObj != null) {
                        for (Object s : samplesObj) {
                            if (s != null) {
                                String text = s.toString().trim();
                                if (!text.isEmpty()) {
                                    oneSamples.add(text);
                                }
                            }
                        }
                    }
                    sampleAnswers.add(oneSamples);
                }
            } else if (questions != null) {
                // 纯题目模式：没有示例答案
                normalizedQuestions.addAll(questions);
            }

            List<WritingTrainingQuestion> saved = writingTrainingService.saveQuestions(
                    userId, moduleKey, moduleTitle, difficulty, normalizedQuestions, sampleAnswers.isEmpty() ? null : sampleAnswers);
            return success(saved);
        } catch (Exception e) {
            log.error("保存写作训练题目失败", e);
            return error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 调用 DeepSeek 生成写作训练题目并保存
     *
     * 请求体：
     * {
     *   moduleKey: string,
     *   moduleTitle?: string,
     *   difficulty?: string, // primary_low/primary_high/middle/high
     *   count?: number       // 1-10，默认 8
     * }
     */
    @Operation(summary = "AI 生成写作训练题目", description = "后端调用 DeepSeek 生成题目及示例答案并保存到数据库")
    @PostMapping("/questions/generate")
    public AjaxResult generateQuestions(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("用户未登录");
            }
            String moduleKey = (String) body.get("moduleKey");
            String moduleTitle = (String) body.get("moduleTitle");
            String difficulty = (String) body.get("difficulty");
            Object countObj = body.get("count");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> templateObjs = (List<Map<String, Object>>) body.get("templates");
            List<String> expressionTemplates = new ArrayList<>();
            if (templateObjs != null) {
                for (Map<String, Object> t : templateObjs) {
                    if (t == null) {
                        continue;
                    }
                    Object labelObj = t.get("label");
                    Object textObj = t.get("text");
                    String label = labelObj != null ? labelObj.toString().trim() : "";
                    String text = textObj != null ? textObj.toString().trim() : "";
                    if (!text.isEmpty()) {
                        String merged = label.isEmpty() ? text : (label + "：" + text);
                        expressionTemplates.add(merged);
                    }
                }
            } else {
                @SuppressWarnings("unchecked")
                List<String> templateStrings = (List<String>) body.get("templates");
                if (templateStrings != null) {
                    for (String t : templateStrings) {
                        if (t != null && !t.trim().isEmpty()) {
                            expressionTemplates.add(t.trim());
                        }
                    }
                }
            }
            int count = 8;
            if (countObj instanceof Number) {
                count = ((Number) countObj).intValue();
            } else if (countObj instanceof String) {
                try {
                    count = Integer.parseInt((String) countObj);
                } catch (NumberFormatException ignore) {
                }
            }

            List<WritingTrainingQuestion> saved = writingTrainingService.generateQuestions(
                    userId, moduleKey, moduleTitle, difficulty, count, expressionTemplates);
            return success(saved);
        } catch (Exception e) {
            log.error("AI 生成写作训练题目失败", e);
            return error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 记录 AI 出题错误日志（供前端在出题失败时调用）
     *
     * POST /writing-training/questions/error
     */
    @Operation(summary = "记录写作训练出题错误日志")
    @PostMapping("/questions/error")
    public AjaxResult logQuestionError(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            String moduleKey = (String) body.get("moduleKey");
            String moduleTitle = (String) body.get("moduleTitle");
            String difficulty = (String) body.get("difficulty");
            String errorMessage = (String) body.get("errorMessage");
            String promptSummary = (String) body.get("promptSummary");
            String responseSummary = (String) body.get("responseSummary");

            WritingTrainingErrorLog log = new WritingTrainingErrorLog();
            log.setUserId(userId);
            log.setModuleKey(moduleKey);
            log.setModuleTitle(moduleTitle);
            log.setDifficulty(difficulty);
            log.setErrorMessage(errorMessage);
            log.setPromptSummary(promptSummary);
            log.setResponseSummary(responseSummary);
            log.setCreateTime(new java.util.Date());

            errorLogMapper.insert(log);
            return success();
        } catch (Exception e) {
            // 这里不能再抛出，让前端静默失败即可
            return error("记录错误日志失败");
        }
    }

    /**
     * 随机获取若干条题目
     *
     * GET /writing-training/questions/random?count=5&moduleKey=...
     */
    @Operation(summary = "随机获取写作训练题目")
    @GetMapping("/questions/random")
    public AjaxResult getRandomQuestions(@RequestParam(defaultValue = "5") Integer count,
                                         @RequestParam(required = false) String moduleKey,
                                         HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            List<WritingTrainingQuestion> list = writingTrainingService.getRandomQuestions(userId, moduleKey, count);
            return success(list);
        } catch (Exception e) {
            log.error("获取写作训练随机题目失败", e);
            return error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询写作训练题目列表
     *
     * GET /writing-training/questions/list?moduleKey=...&difficulty=...&pageNum=1&pageSize=20
     */
    @Operation(summary = "分页查询写作训练题目列表")
    @GetMapping("/questions/list")
    public AjaxResult getQuestionPage(@RequestParam(required = false) String moduleKey,
                                      @RequestParam(required = false) String difficulty,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "20") Integer pageSize,
                                      HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            Page<WritingTrainingQuestion> page = new Page<>(pageNum, pageSize);
            IPage<WritingTrainingQuestion> result = writingTrainingService.getQuestionPage(page, userId, moduleKey, difficulty);

            Map<String, Object> data = new HashMap<>();
            data.put("rows", result.getRecords());
            data.put("total", result.getTotal());

            return success(data);
        } catch (Exception e) {
            log.error("查询写作训练题目列表失败", e);
            return error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存写作训练记录
     *
     * 请求体：{ moduleKey, moduleTitle, difficulty, questionId, content }
     */
    @Operation(summary = "保存写作训练记录")
    @PostMapping("/records")
    public AjaxResult saveRecord(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("用户未登录");
            }
            String moduleKey = (String) body.get("moduleKey");
            String moduleTitle = (String) body.get("moduleTitle");
            String difficulty = (String) body.get("difficulty");
            Object qid = body.get("questionId");
            Long questionId = null;
            if (qid instanceof Number) {
                questionId = ((Number) qid).longValue();
            } else if (qid instanceof String) {
                try {
                    questionId = Long.parseLong((String) qid);
                } catch (NumberFormatException ignore) {
                }
            }
            String content = (String) body.get("content");

            WritingTrainingRecord saved = writingTrainingService.saveRecord(userId, moduleKey, moduleTitle, difficulty, questionId, content);
            return success(saved);
        } catch (Exception e) {
            log.error("保存写作训练记录失败", e);
            return error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询训练记录
     *
     * GET /writing-training/records/list?moduleKey=...&difficulty=...&pageNum=1&pageSize=20
     */
    @Operation(summary = "分页查询写作训练记录")
    @GetMapping("/records/list")
    public AjaxResult getRecordPage(@RequestParam(required = false) String moduleKey,
                                    @RequestParam(required = false) String difficulty,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "20") Integer pageSize,
                                    HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("用户未登录");
            }
            Page<WritingTrainingRecord> page = new Page<>(pageNum, pageSize);
            IPage<WritingTrainingRecord> result = writingTrainingService.getRecordPage(page, userId, moduleKey, difficulty);

            Map<String, Object> data = new HashMap<>();
            data.put("rows", result.getRecords());
            data.put("total", result.getTotal());

            return success(data);
        } catch (Exception e) {
            log.error("查询写作训练记录失败", e);
            return error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据题目 ID 查询该题目的所有训练记录（当前登录用户）
     *
     * GET /writing-training/records/by-question?questionId=...
     */
    @Operation(summary = "根据题目 ID 查询训练记录")
    @GetMapping("/records/by-question")
    public AjaxResult getRecordsByQuestion(@RequestParam Long questionId, HttpServletRequest request) {
        try {
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return error("用户未登录");
            }
            List<WritingTrainingRecord> list = writingTrainingService.getRecordsByQuestionId(userId, questionId);
            return success(list);
        } catch (Exception e) {
            log.error("根据题目 ID 查询写作训练记录失败", e);
            return error("查询失败：" + e.getMessage());
        }
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
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
