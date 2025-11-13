package com.gameengine.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.framework.web.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 测试控制器
 * 
 * @author GameEngine
 */
@Tag(name = "测试接口", description = "系统测试接口")
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
    
    /**
     * 测试接口
     */
    @Operation(summary = "测试接口", description = "返回 Hello 消息")
    @GetMapping("/hello")
    public AjaxResult hello() {
        return success("Hello, GameEngine Manager!");
    }
}

