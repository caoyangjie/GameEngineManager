package com.gameengine.system.domain.dto;

/**
 * 文字专注力生成请求
 */
public class TextFocusGenerateRequest {

    /** 生成条数，默认 10 */
    private Integer count;

    /** 主题或场景提示 */
    private String theme;

    /** 难度等级：初/中/高/困 */
    private String level;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

