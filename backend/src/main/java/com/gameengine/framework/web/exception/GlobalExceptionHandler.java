package com.gameengine.framework.web.exception;

import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gameengine.common.core.domain.AjaxResult;
import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.common.utils.MessageUtils;

/**
 * 全局异常处理器
 * 
 * @author GameEngine
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request) {
        Integer code = e.getCode();
        String message = e.getMessage();
        // 如果消息是国际化键，则获取国际化消息
        String i18nMessage = MessageUtils.message(message);
        // 如果获取到的消息与键相同，说明键不存在，使用原始消息
        // 国际化键通常包含点号，如果消息不包含点号，可能是普通消息而不是键
        if (i18nMessage.equals(message) && !message.contains(".")) {
            i18nMessage = message;
        }
        log.error("请求地址'{}',发生业务异常.", request.getRequestURI(), e);
        if (code != null) {
            return AjaxResult.error(code, i18nMessage);
        }
        return AjaxResult.error(i18nMessage);
    }
    
    /**
     * 参数校验异常 - @RequestBody + @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("请求地址'{}',参数校验失败.", request.getRequestURI(), e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> MessageUtils.message("field.error.format", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return AjaxResult.error(400, MessageUtils.message("exception.param.validate.failed") + ": " + message);
    }
    
    /**
     * 参数校验异常 - @ModelAttribute + @Valid
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e, HttpServletRequest request) {
        log.error("请求地址'{}',参数绑定失败.", request.getRequestURI(), e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> MessageUtils.message("field.error.format", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return AjaxResult.error(400, MessageUtils.message("exception.param.bind.failed") + ": " + message);
    }
    
    /**
     * 参数校验异常 - @RequestParam + @Valid
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public AjaxResult handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.error("请求地址'{}',参数校验失败.", request.getRequestURI(), e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream()
                .map(violation -> MessageUtils.message("field.error.format", violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.joining("; "));
        return AjaxResult.error(400, MessageUtils.message("exception.param.validate.failed") + ": " + message);
    }
    
    /**
     * 请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求地址'{}',不支持'{}'请求方法.", request.getRequestURI(), e.getMethod(), e);
        String message = MessageUtils.message("exception.method.not.supported") + ": " + e.getMethod() + ", " + 
                         MessageUtils.message("exception.method.supported") + ": " + String.join(", ", e.getSupportedMethods());
        return AjaxResult.error(405, message);
    }
    
    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生运行时异常.", request.getRequestURI(), e);
        return AjaxResult.error(500, MessageUtils.message("exception.runtime") + ": " + e.getMessage());
    }
    
    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleException(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生系统异常.", request.getRequestURI(), e);
        return AjaxResult.error(500, MessageUtils.message("exception.system") + ": " + e.getMessage());
    }
}

