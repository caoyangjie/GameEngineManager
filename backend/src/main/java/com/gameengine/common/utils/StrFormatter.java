package com.gameengine.common.utils;

/**
 * 字符串格式化工具类
 * 
 * @author GameEngine
 */
public class StrFormatter {
    
    public static final String EMPTY_JSON = "{}";
    public static final char C_BACKSLASH = '\\';
    public static final char C_DELIM_START = '{';
    public static final char C_DELIM_END = '}';
    
    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     * 
     * @param strPattern 字符串模板
     * @param argArray 参数列表
     * @return 结果
     */
    public static String format(final String strPattern, final Object... argArray) {
        if (StringUtils.isEmpty(strPattern) || StringUtils.isEmpty(argArray)) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();
        
        // 初始化定义好的长度以获得更好的性能
        StringBuilder sbuf = new StringBuilder(strPatternLength + 50);
        
        int handledPosition = 0;
        int argIndex = 0;
        for (int delimIndex = 0; delimIndex < strPatternLength - 1; delimIndex++) {
            final char escapeChar = strPattern.charAt(delimIndex);
            if (C_BACKSLASH == escapeChar) {
                if (C_DELIM_START == strPattern.charAt(delimIndex + 1)) {
                    delimIndex++;
                    sbuf.append(C_DELIM_START);
                    handledPosition = delimIndex + 1;
                } else {
                    sbuf.append(escapeChar);
                    handledPosition = delimIndex + 1;
                }
            } else if (C_DELIM_START == escapeChar) {
                if (C_DELIM_END == strPattern.charAt(delimIndex + 1)) {
                    if (handledPosition != delimIndex) {
                        sbuf.append(strPattern, handledPosition, delimIndex);
                    }
                    if (argIndex < argArray.length) {
                        sbuf.append(argArray[argIndex++]);
                    } else {
                        sbuf.append(EMPTY_JSON);
                    }
                    delimIndex++;
                    handledPosition = delimIndex + 1;
                } else {
                    sbuf.append(escapeChar);
                    handledPosition = delimIndex + 1;
                }
            }
        }
        
        if (handledPosition != strPatternLength) {
            sbuf.append(strPattern, handledPosition, strPatternLength);
        }
        
        return sbuf.toString();
    }
}

