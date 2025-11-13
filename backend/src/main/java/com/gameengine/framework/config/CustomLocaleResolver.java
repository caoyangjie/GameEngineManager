package com.gameengine.framework.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 自定义国际化解析器
 * 支持从请求头中获取语言设置，优先级：
 * 1. 自定义请求头 "lang" 或 "locale"
 * 2. Accept-Language 请求头
 * 3. 默认语言
 * 
 * @author GameEngine
 */
public class CustomLocaleResolver implements LocaleResolver {
    
    /**
     * 默认语言
     */
    private static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;
    
    /**
     * 支持的语言列表
     */
    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            Locale.SIMPLIFIED_CHINESE,
            Locale.ENGLISH
    );
    
    /**
     * 自定义语言请求头名称（优先级最高）
     */
    private static final String LANG_HEADER = "lang";
    
    /**
     * 备用语言请求头名称
     */
    private static final String LOCALE_HEADER = "locale";
    
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 1. 优先从自定义请求头 "lang" 获取
        String lang = request.getHeader(LANG_HEADER);
        if (StringUtils.hasText(lang)) {
            Locale locale = parseLocale(lang);
            if (isSupported(locale)) {
                return locale;
            }
        }
        
        // 2. 从备用请求头 "locale" 获取
        String locale = request.getHeader(LOCALE_HEADER);
        if (StringUtils.hasText(locale)) {
            Locale parsedLocale = parseLocale(locale);
            if (isSupported(parsedLocale)) {
                return parsedLocale;
            }
        }
        
        // 3. 从 Accept-Language 请求头获取
        String acceptLanguage = request.getHeader("Accept-Language");
        if (StringUtils.hasText(acceptLanguage)) {
            Locale parsedLocale = parseLocale(acceptLanguage);
            if (isSupported(parsedLocale)) {
                return parsedLocale;
            }
        }
        
        // 4. 返回默认语言
        return DEFAULT_LOCALE;
    }
    
    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // 不支持设置，使用只读模式
    }
    
    /**
     * 解析语言字符串为 Locale 对象
     * 支持格式：zh-CN, zh_CN, en, en-US 等
     */
    private Locale parseLocale(String localeString) {
        if (!StringUtils.hasText(localeString)) {
            return DEFAULT_LOCALE;
        }
        
        // 去除空格并转换为小写
        localeString = localeString.trim().toLowerCase();
        
        // 处理 Accept-Language 格式（可能包含多个语言，用逗号分隔，如：zh-CN,zh;q=0.9,en;q=0.8）
        if (localeString.contains(",")) {
            localeString = localeString.split(",")[0].trim();
        }
        
        // 处理质量值（如：zh-CN;q=0.9）
        if (localeString.contains(";")) {
            localeString = localeString.split(";")[0].trim();
        }
        
        // 替换下划线为连字符
        localeString = localeString.replace("_", "-");
        
        // 解析语言代码
        String[] parts = localeString.split("-");
        if (parts.length >= 2) {
            // 有国家代码，如：zh-CN, en-US
            return new Locale(parts[0], parts[1]);
        } else {
            // 只有语言代码，如：zh, en
            return new Locale(parts[0]);
        }
    }
    
    /**
     * 检查语言是否支持
     */
    private boolean isSupported(Locale locale) {
        if (locale == null) {
            return false;
        }
        
        // 检查是否在支持列表中
        for (Locale supportedLocale : SUPPORTED_LOCALES) {
            if (supportedLocale.getLanguage().equals(locale.getLanguage())) {
                return true;
            }
        }
        
        return false;
    }
}

