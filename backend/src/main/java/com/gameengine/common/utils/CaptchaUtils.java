package com.gameengine.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 验证码工具类
 * 
 * @author GameEngine
 */
@Component
public class CaptchaUtils {
    
    /** 验证码类型：数学计算 */
    private static final String CAPTCHA_TYPE_MATH = "math";
    
    /** 验证码类型：字符验证 */
    private static final String CAPTCHA_TYPE_CHAR = "char";
    
    /** 验证码宽度 */
    private static final int WIDTH = 140;
    
    /** 验证码高度 */
    private static final int HEIGHT = 40;
    
    /** 验证码字符数 */
    private static final int CODE_LENGTH = 4;
    
    /** 字体大小 */
    private static final int FONT_SIZE = 18;
    
    /** 图片边距 */
    private static final int PADDING = 30;
    
    @Value("${gameengine.captchaType:math}")
    private String captchaType;
    
    /**
     * 生成数学计算验证码
     * 
     * @return 验证码信息 [验证码文本, Base64图片]
     */
    public String[] generateMathCaptcha() {
        Random random = new Random();
        int num1 = random.nextInt(10) + 1;
        int num2 = random.nextInt(10) + 1;
        int operator = random.nextInt(3);
        
        int result;
        String expression;
        if (operator == 0) {
            result = num1 + num2;
            expression = num1 + " + " + num2 + " = ?";
        } else if (operator == 1) {
            if (num1 < num2) {
                int temp = num1;
                num1 = num2;
                num2 = temp;
            }
            result = num1 - num2;
            expression = num1 + " - " + num2 + " = ?";
        } else if (operator == 2) {
            result = num1 * num2;
            expression = num1 + " * " + num2 + " = ?";
        }
        else {
            result = num1 % num2;
            expression = num1 + " % " + num2 + " = ?";
        }
        
        String code = String.valueOf(result);
        String img = generateImage(expression);
        
        return new String[]{code, img};
    }
    
    /**
     * 生成字符验证码
     * 
     * @return 验证码信息 [验证码文本, Base64图片]
     */
    public String[] generateCharCaptcha() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 排除容易混淆的字符
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        String img = generateImage(code.toString());
        
        return new String[]{code.toString(), img};
    }
    
    /**
     * 生成验证码（根据配置类型）
     * 
     * @return 验证码信息 [验证码文本, Base64图片]
     */
    public String[] generateCaptcha() {
        if (CAPTCHA_TYPE_MATH.equals(captchaType)) {
            return generateMathCaptcha();
        } else {
            return generateCharCaptcha();
        }
    }
    
    /**
     * 生成验证码图片
     * 
     * @param text 验证码文本
     * @return Base64编码的图片
     */
    private String generateImage(String text) {
        // 设置字体
        Font font = new Font("Arial", Font.BOLD, FONT_SIZE);
        
        // 创建临时 Graphics 来计算文本宽度
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D tempG = tempImage.createGraphics();
        tempG.setFont(font);
        FontMetrics fm = tempG.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        tempG.dispose();
        
        // 根据文本实际宽度动态计算图片宽度，留出边距
        int imageWidth = Math.max(WIDTH, textWidth + PADDING);
        BufferedImage image = new BufferedImage(imageWidth, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 启用抗锯齿
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                          java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, HEIGHT);
        
        // 设置字体
        g.setFont(font);
        
        // 重新获取 FontMetrics（因为 Graphics 对象不同）
        fm = g.getFontMetrics(font);
        int startX = (imageWidth - textWidth) / 2;
        
        // 绘制验证码文本
        Random random = new Random();
        int currentX = startX;
        int baseY = HEIGHT / 2 + FONT_SIZE / 3;
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            int charWidth = fm.charWidth(ch);
            
            // 设置随机颜色
            g.setColor(new Color(random.nextInt(100) + 50, random.nextInt(100) + 50, random.nextInt(100) + 50));
            
            // 添加轻微的垂直偏移
            int offsetY = random.nextInt(6) - 3;
            g.drawString(String.valueOf(ch), currentX, baseY + offsetY);
            
            // 移动到下一个字符位置
            currentX += charWidth;
        }
        
        // 绘制干扰线
        for (int i = 0; i < 3; i++) {
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            g.drawLine(random.nextInt(imageWidth), random.nextInt(HEIGHT), 
                      random.nextInt(imageWidth), random.nextInt(HEIGHT));
        }
        
        // 绘制干扰点
        for (int i = 0; i < 20; i++) {
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            g.fillOval(random.nextInt(imageWidth), random.nextInt(HEIGHT), 2, 2);
        }
        
        g.dispose();
        
        // 转换为Base64
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
    }
}

