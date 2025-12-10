package com.gameengine.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.gameengine.common.core.exception.ServiceException;

/**
 * Simple in-memory captcha store shared across controllers.
 */
public class CaptchaStore {

    private static final Map<String, CaptchaInfo> CAPTCHA_STORE = new ConcurrentHashMap<>();

    private static final int DEFAULT_EXPIRE_MINUTES = 5;

    private CaptchaStore() {
    }

    private static class CaptchaInfo {
        final String code;
        final long expireTime;

        CaptchaInfo(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    public static void store(String uuid, String code) {
        store(uuid, code, DEFAULT_EXPIRE_MINUTES);
    }

    public static void store(String uuid, String code, int expireMinutes) {
        long expireTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expireMinutes);
        CAPTCHA_STORE.put(uuid, new CaptchaInfo(code, expireTime));
        cleanupExpired();
    }

    public static void validate(String uuid, String code, String expiredMsg, String errorMsg) {
        CaptchaInfo captchaInfo = CAPTCHA_STORE.get(uuid);
        if (captchaInfo == null) {
            throw new ServiceException(expiredMsg, 400);
        }
        if (captchaInfo.isExpired()) {
            CAPTCHA_STORE.remove(uuid);
            throw new ServiceException(expiredMsg, 400);
        }
        if (!captchaInfo.code.equalsIgnoreCase(code)) {
            throw new ServiceException(errorMsg, 400);
        }
    }

    public static void remove(String uuid) {
        CAPTCHA_STORE.remove(uuid);
    }

    private static void cleanupExpired() {
        CAPTCHA_STORE.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}

