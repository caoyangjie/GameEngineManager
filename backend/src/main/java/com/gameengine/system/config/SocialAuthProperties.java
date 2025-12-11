package com.gameengine.system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 第三方登录配置
 */
@Component
@ConfigurationProperties(prefix = "social.auth")
public class SocialAuthProperties {

    /**
     * 回调基础地址，如 http://localhost:18080/gameweb/oauth/callback
     */
    private String redirectBase;

    private ProviderConfig wechat = new ProviderConfig();
    private ProviderConfig qq = new ProviderConfig();
    private ProviderConfig feishu = new ProviderConfig();
    private ProviderConfig dingtalk = new ProviderConfig();

    public String getRedirectBase() {
        return redirectBase;
    }

    public void setRedirectBase(String redirectBase) {
        this.redirectBase = redirectBase;
    }

    public ProviderConfig getWechat() {
        return wechat;
    }

    public void setWechat(ProviderConfig wechat) {
        this.wechat = wechat;
    }

    public ProviderConfig getQq() {
        return qq;
    }

    public void setQq(ProviderConfig qq) {
        this.qq = qq;
    }

    public ProviderConfig getFeishu() {
        return feishu;
    }

    public void setFeishu(ProviderConfig feishu) {
        this.feishu = feishu;
    }

    public ProviderConfig getDingtalk() {
        return dingtalk;
    }

    public void setDingtalk(ProviderConfig dingtalk) {
        this.dingtalk = dingtalk;
    }

    public static class ProviderConfig {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        /**
         * QQ unionid 开关等个别平台特殊字段
         */
        private boolean unionId;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        public boolean isUnionId() {
            return unionId;
        }

        public void setUnionId(boolean unionId) {
            this.unionId = unionId;
        }
    }
}


