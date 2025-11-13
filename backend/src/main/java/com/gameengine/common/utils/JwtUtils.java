package com.gameengine.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWT工具类
 * 
 * @author GameEngine
 */
@Component
public class JwtUtils {
    
    /** HS512 算法要求的最小密钥长度（512 位 = 64 字节） */
    private static final int MIN_KEY_LENGTH = 64;
    
    /** 令牌自定义标识 */
    @Value("${token.header:Authorization}")
    private String header;
    
    /** 令牌密钥 */
    @Value("${token.secret}")
    private String secret;
    
    /** 令牌有效期（默认30分钟） */
    @Value("${token.expireTime:30}")
    private int expireTime;
    
    /**
     * 获取签名密钥
     * 确保密钥长度满足 HS512 算法要求（至少 512 位/64 字节）
     * Keys.hmacShaKeyFor() 会自动处理密钥，确保满足算法要求
     * 
     * @return SecretKey
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        
        // 如果密钥长度不足，使用 SHA-512 哈希来扩展密钥
        // 这样可以确保密钥长度满足要求，同时保持安全性
        if (keyBytes.length < MIN_KEY_LENGTH) {
            // 使用 SHA-512 对密钥进行哈希，生成 64 字节的密钥
            java.security.MessageDigest digest;
            try {
                digest = java.security.MessageDigest.getInstance("SHA-512");
                keyBytes = digest.digest(keyBytes);
            } catch (java.security.NoSuchAlgorithmException e) {
                // 如果 SHA-512 不可用，使用 Keys.hmacShaKeyFor 自动处理
                // 它会使用 HMAC-SHA-512 来派生密钥
            }
        }
        
        // Keys.hmacShaKeyFor 会自动确保密钥满足算法要求
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * 获取用户身份信息
     * 
     * @param token 令牌
     * @return 用户信息
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 创建令牌
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @return 令牌
     */
    public String createToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims);
    }
    
    /**
     * 从数据声明生成令牌
     * 
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireTime * 60 * 1000);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 从令牌中获取用户名
     * 
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? (String) claims.get("username") : null;
    }
    
    /**
     * 从令牌中获取用户ID
     * 
     * @param token 令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? ((Number) claims.get("userId")).longValue() : null;
    }
    
    /**
     * 验证令牌是否过期
     * 
     * @param token 令牌
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return true;
        }
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
    
    /**
     * 获取令牌的剩余有效时间（秒）
     * 
     * @param token 令牌
     * @return 剩余有效时间
     */
    public long getRemainingTime(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return 0;
        }
        Date expiration = claims.getExpiration();
        long remaining = expiration.getTime() - System.currentTimeMillis();
        return remaining > 0 ? remaining / 1000 : 0;
    }
}

