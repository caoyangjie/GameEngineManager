package com.gameengine.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 字符串工具类
 * 
 * @author GameEngine
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    
    /** 空字符串 */
    private static final String NULLSTR = "";
    
    /** 下划线 */
    private static final char SEPARATOR = '_';
    
    /**
     * 获取参数不为空值
     * 
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
    
    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }
    
    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
    
    /**
     * * 判断一个对象数组是否为空
     * 
     * @param objects 要判断的对象数组
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }
    
    /**
     * * 判断一个对象数组是否非空
     * 
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }
    
    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }
    
    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    /**
     * * 判断一个字符串是否为空串
     * 
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }
    
    /**
     * * 判断一个字符串是否为非空串
     * 
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * * 判断一个对象是否为空
     * 
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }
    
    /**
     * * 判断一个对象是否非空
     * 
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }
    
    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     * 
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }
    
    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }
    
    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }
        
        if (start < 0) {
            start = str.length() + start;
        }
        
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }
        
        return str.substring(start);
    }
    
    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @param end 结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return NULLSTR;
        }
        
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        
        if (end > str.length()) {
            end = str.length();
        }
        
        if (start > end) {
            return NULLSTR;
        }
        
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        
        return str.substring(start, end);
    }
    
    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     * 
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params) {
        if (isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }
    
    /**
     * 字符串转date类型
     * 
     * @param str 日期字符串
     * @return 日期
     */
    public static Date parseDate(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            if (str.contains(":")) {
                SimpleDateFormat formatter;
                if (str.contains("-")) {
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                }
                return formatter.parse(str);
            } else {
                SimpleDateFormat formatter;
                if (str.contains("-")) {
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                } else {
                    formatter = new SimpleDateFormat("yyyyMMdd");
                }
                return formatter.parse(str);
            }
        } catch (ParseException e) {
            return null;
        }
    }
}

