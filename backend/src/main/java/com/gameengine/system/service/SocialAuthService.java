package com.gameengine.system.service;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.gameengine.common.core.exception.ServiceException;
import com.gameengine.common.utils.JwtUtils;
import com.gameengine.common.utils.SecurityUtils;
import com.gameengine.system.config.SocialAuthProperties;
import com.gameengine.system.domain.SysSocialUser;
import com.gameengine.system.domain.SysUser;
import com.gameengine.system.domain.dto.SocialAuthLoginResult;
import com.gameengine.system.domain.dto.SocialAuthorizeInfo;
import com.gameengine.system.mapper.SysUserMapper;

import cn.hutool.crypto.digest.DigestUtil;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDingTalkRequest;
import me.zhyd.oauth.request.AuthFeishuRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import me.zhyd.oauth.utils.AuthStateUtils;

/**
 * 第三方登录服务
 */
@Service
public class SocialAuthService {

    private static final String DEFAULT_REDIRECT_BASE = "http://localhost:18080/gameweb/oauth/callback";

    @Autowired
    private SocialAuthProperties socialAuthProperties;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysSocialUserService socialUserService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 生成授权地址
     */
    public SocialAuthorizeInfo buildAuthorizeInfo(String source) {
        AuthRequest authRequest = buildAuthRequest(source);
        String state = AuthStateUtils.createState();
        SocialAuthorizeInfo info = new SocialAuthorizeInfo();
        info.setAuthorizeUrl(authRequest.authorize(state));
        info.setState(state);
        info.setSource(normalizeSource(source));
        return info;
    }

    /**
     * 处理第三方回调
     */
    public SocialAuthLoginResult handleCallback(String source, AuthCallback callback) {
        AuthRequest authRequest = buildAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        if (response == null || !response.ok()) {
            String msg = response == null ? "第三方登录失败" : response.getMsg();
            throw new ServiceException("第三方登录失败: " + msg);
        }
        AuthUser authUser = response.getData();
        if (authUser == null) {
            throw new ServiceException("第三方登录未返回用户信息");
        }
        String normalizedSource = normalizeSource(source);
        SysUser user = resolveAndBindUser(normalizedSource, authUser);

        SocialAuthLoginResult result = new SocialAuthLoginResult();
        result.setSource(normalizedSource);
        result.setUserId(user.getUserId());
        result.setUserName(user.getUserName());
        result.setNickName(user.getNickName());
        result.setToken(jwtUtils.createToken(user.getUserId(), user.getUserName()));
        return result;
    }

    private SysUser resolveAndBindUser(String source, AuthUser authUser) {
        String uuid = authUser.getUuid();
        if (StringUtils.isBlank(uuid)) {
            throw new ServiceException("第三方用户标识为空");
        }
        String unionId = authUser.getToken() != null ? authUser.getToken().getUnionId() : null;

        SysSocialUser binding = socialUserService.findBySourceAndUuid(source, uuid);
        if (binding == null && StringUtils.isNotBlank(unionId)) {
            binding = socialUserService.findBySourceAndUnionId(source, unionId);
        }

        SysUser user = binding != null ? userService.selectUserById(binding.getUserId()) : null;

        if (user == null && StringUtils.isNotBlank(authUser.getEmail())) {
            user = userService.selectUserByEmail(authUser.getEmail());
        }
        if (user == null && StringUtils.isNotBlank(authUser.getUsername())) {
            user = userService.selectUserByUserName(authUser.getUsername());
        }

        if (user == null) {
            user = createUserFromAuth(source, authUser);
        }

        SysSocialUser toSave = binding != null ? binding : new SysSocialUser();
        if (binding == null) {
            toSave.setSource(source);
            toSave.setUuid(uuid);
        }
        toSave.setUnionId(StringUtils.defaultIfBlank(unionId, toSave.getUnionId()));
        toSave.setUserId(user.getUserId());
        toSave.setUsername(StringUtils.defaultIfBlank(authUser.getNickname(), authUser.getUsername()));
        toSave.setAvatar(authUser.getAvatar());
        toSave.setEmail(authUser.getEmail());
        if (authUser.getToken() != null) {
            toSave.setAccessToken(authUser.getToken().getAccessToken());
            toSave.setRefreshToken(authUser.getToken().getRefreshToken());
            Long expireIn = Long.valueOf(authUser.getToken().getExpireIn());
            toSave.setExpireAt(expireIn > 0 ? new Date(System.currentTimeMillis() + expireIn * 1000L) : null);
        }
        if (authUser.getRawUserInfo() != null) {
            toSave.setRawUserInfo(JSON.toJSONString(authUser.getRawUserInfo()));
        }
        socialUserService.saveOrUpdate(toSave);
        return user;
    }

    private SysUser createUserFromAuth(String source, AuthUser authUser) {
        SysUser user = new SysUser();
        String uuid = StringUtils.defaultIfBlank(authUser.getUuid(), UUID.randomUUID().toString());
        String digest = DigestUtil.sha1Hex(uuid);
        String shortDigest = digest.length() > 16 ? digest.substring(0, 16) : digest;
        user.setUserName(source + "_" + shortDigest);
        String nickname = StringUtils.defaultIfBlank(authUser.getNickname(), authUser.getUsername());
        user.setNickName(StringUtils.defaultIfBlank(nickname, source + "_user"));
        user.setEmail(StringUtils.defaultIfBlank(authUser.getEmail(), ""));
        user.setAvatar(authUser.getAvatar());
        user.setPassword(SecurityUtils.encryptPassword(UUID.randomUUID().toString()));
        user.setStatus("0");
        user.setDelFlag("0");
        user.setUserCategory("player");
        user.setCreateTime(new Date());
        user.setRemark("Created via " + source + " login");
        userMapper.insert(user);
        return user;
    }

    private AuthRequest buildAuthRequest(String source) {
        String normalizedSource = normalizeSource(source);
        AuthConfig authConfig = buildAuthConfig(normalizedSource);
        switch (normalizedSource) {
            case "wechat":
                return new AuthWeChatOpenRequest(authConfig);
            case "qq":
                return new AuthQqRequest(authConfig);
            case "feishu":
                return new AuthFeishuRequest(authConfig);
            case "dingtalk":
                return new AuthDingTalkRequest(authConfig);
            default:
                throw new ServiceException("不支持的第三方平台: " + normalizedSource);
        }
    }

    private AuthConfig buildAuthConfig(String source) {
        SocialAuthProperties.ProviderConfig provider = getProviderConfig(source);
        if (provider == null || StringUtils.isAnyBlank(provider.getClientId(), provider.getClientSecret())) {
            throw new ServiceException("未配置第三方登录参数: " + source);
        }
        String redirectUri = provider.getRedirectUri();
        if (StringUtils.isBlank(redirectUri)) {
            redirectUri = buildDefaultRedirectUri(source);
        }
        try {
            return AuthConfig.builder()
                .clientId(provider.getClientId())
                .clientSecret(provider.getClientSecret())
                .redirectUri(redirectUri)
                .unionId(provider.isUnionId())
                .build();
        } catch (AuthException ex) {
            throw new ServiceException("第三方登录参数错误: " + ex.getMessage());
        }
    }

    private SocialAuthProperties.ProviderConfig getProviderConfig(String source) {
        switch (source) {
            case "wechat":
                return socialAuthProperties.getWechat();
            case "qq":
                return socialAuthProperties.getQq();
            case "feishu":
                return socialAuthProperties.getFeishu();
            case "dingtalk":
                return socialAuthProperties.getDingtalk();
            default:
                return null;
        }
    }

    private String buildDefaultRedirectUri(String source) {
        String base = StringUtils.defaultIfBlank(socialAuthProperties.getRedirectBase(), DEFAULT_REDIRECT_BASE);
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/" + source;
    }

    private String normalizeSource(String source) {
        if (StringUtils.isBlank(source)) {
            throw new ServiceException("缺少第三方平台标识");
        }
        return source.toLowerCase(Locale.ROOT);
    }
}


