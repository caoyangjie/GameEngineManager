package com.gameengine.common.utils;

/**
 * 密码生成工具类（用于生成初始密码）
 * 运行此类的 main 方法可以生成 BCrypt 加密后的密码
 * 
 * @author GameEngine
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        String password = "admin123";
        String encoded = SecurityUtils.encryptPassword(password);
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt 加密后: " + encoded);
    }
}

