package com.gameengine.common.utils;

/**
 * 旅程等级工具类
 * 
 * @author GameEngine
 */
public class JourneyLevelUtils {
    
    /**
     * 旅程等级映射
     * 0 -> 黑铁
     * 1 -> 青铜
     * 2 -> 白银
     * 3 -> 黄金
     * 4 -> 白金
     * 5 -> 翡翠
     * 6 -> 钻石
     */
    private static final String[] LEVEL_NAMES = {
        "黑铁", "青铜", "白银", "黄金", "白金", "翡翠", "钻石"
    };
    
    /**
     * 将数字等级转换为等级名称
     * 
     * @param level 数字等级（0-6），可以是字符串或数字
     * @return 等级名称，如果输入无效则返回原值
     */
    public static String convertLevelToName(String level) {
        if (level == null || level.trim().isEmpty()) {
            return "";
        }
        
        try {
            int levelNum = Integer.parseInt(level.trim());
            if (levelNum >= 0 && levelNum < LEVEL_NAMES.length) {
                return LEVEL_NAMES[levelNum];
            }
        } catch (NumberFormatException e) {
            // 如果不是数字，可能是已经是等级名称，直接返回
            return level;
        }
        
        // 如果数字超出范围，返回原值
        return level;
    }
    
    /**
     * 将数字等级转换为等级名称
     * 
     * @param level 数字等级（0-6）
     * @return 等级名称，如果输入无效则返回空字符串
     */
    public static String convertLevelToName(Integer level) {
        if (level == null) {
            return "";
        }
        
        if (level >= 0 && level < LEVEL_NAMES.length) {
            return LEVEL_NAMES[level];
        }
        
        return "";
    }
    
    /**
     * 将等级名称转换为数字等级
     * 
     * @param levelName 等级名称（黑铁、青铜、白银、黄金、白金、翡翠、钻石）
     * @return 数字等级（0-6），如果输入无效则返回原值
     */
    public static String convertNameToLevel(String levelName) {
        if (levelName == null || levelName.trim().isEmpty()) {
            return "";
        }
        
        String trimmedName = levelName.trim();
        
        // 遍历等级名称数组，找到匹配的索引
        for (int i = 0; i < LEVEL_NAMES.length; i++) {
            if (LEVEL_NAMES[i].equals(trimmedName)) {
                return String.valueOf(i);
            }
        }
        
        // 如果找不到匹配的名称，检查是否是数字字符串
        try {
            int levelNum = Integer.parseInt(trimmedName);
            if (levelNum >= 0 && levelNum < LEVEL_NAMES.length) {
                // 已经是有效的数字，直接返回
                return trimmedName;
            }
        } catch (NumberFormatException e) {
            // 不是数字，也不是有效的等级名称，返回原值
        }
        
        // 如果既不是有效的等级名称，也不是有效的数字，返回原值
        return levelName;
    }
}

