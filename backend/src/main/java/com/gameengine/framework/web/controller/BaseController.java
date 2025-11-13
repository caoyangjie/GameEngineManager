package com.gameengine.framework.web.controller;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * web层通用数据处理
 * 
 * @author GameEngine
 */
public class BaseController {
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(StringUtils.parseDate(text));
            }
        });
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data) {
        return AjaxResult.success(data);
    }
    
    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }
    
    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }
    
    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message) {
        return AjaxResult.warn(message);
    }
}

