package com.gameengine.common.core.exception;

/**
 * 业务异常
 * 
 * @author GameEngine
 */
public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /** 错误码 */
    private Integer code;
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Integer code) {
        super(message);
        this.code = code;
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceException(String message, Integer code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public Integer getCode() {
        return code;
    }
}

