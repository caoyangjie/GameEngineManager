package com.gameengine.system.domain.dto;

/**
 * 第三方登录授权地址返回
 */
public class SocialAuthorizeInfo {

    private String authorizeUrl;
    private String state;
    private String source;

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}


