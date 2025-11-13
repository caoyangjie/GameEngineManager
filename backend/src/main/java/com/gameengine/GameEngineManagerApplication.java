package com.gameengine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 游戏引擎管理系统启动类
 * 
 * @author GameEngine
 */
@SpringBootApplication
@MapperScan("com.gameengine.**.mapper")
public class GameEngineManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameEngineManagerApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  游戏引擎管理系统启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}

