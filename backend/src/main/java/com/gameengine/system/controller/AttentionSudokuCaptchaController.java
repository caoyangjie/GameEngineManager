package com.gameengine.system.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.CaptchaStore;
import com.gameengine.common.utils.MessageUtils;
import com.gameengine.framework.web.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数独验证码校验接口
 */
@Tag(name = "数独验证码", description = "数独查看答案验证码校验")
@RestController
@RequestMapping("/attention/sudoku")
public class AttentionSudokuCaptchaController extends BaseController {

    @Operation(summary = "校验数独验证码", description = "校验查看答案时输入的验证码")
    @PostMapping("/verifyCaptcha")
    public AjaxResult verifyCaptcha(@RequestBody Map<String, String> body) {
        String uuid = body.get("uuid");
        String code = body.get("code");
        if (uuid == null || uuid.trim().isEmpty()) {
            return error("验证码标识不能为空");
        }
        if (code == null || code.trim().isEmpty()) {
            return error("验证码不能为空");
        }

        CaptchaStore.validate(uuid, code, MessageUtils.message("captcha.expired"), MessageUtils.message("captcha.error"));
        CaptchaStore.remove(uuid);
        return success("验证通过");
    }
}

