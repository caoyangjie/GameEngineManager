package com.gameengine.system.controller;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.framework.web.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 
 * @author GameEngine
 */
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
    
    /**
     * 测试接口
     */
    @GetMapping("/hello")
    public AjaxResult hello() {
        return success("Hello, GameEngine Manager!");
    }
}

