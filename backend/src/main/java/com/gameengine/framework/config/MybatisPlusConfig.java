package com.gameengine.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * MyBatis-Plus 配置
 *
 * 说明：
 * 1. 从 MyBatis-Plus 3.4.x 开始，分页插件需要通过 MybatisPlusInterceptor 显式配置；
 * 2. 如果没有注册 PaginationInnerInterceptor，调用 selectPage 时 SQL 不会自动追加 LIMIT/OFFSET，
 *    也不会自动执行 count 查询，导致你看到的就是一个普通的 SELECT 语句且 total 可能为 0。
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 使用 MySQL 数据库的分页方言
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 防止 pageSize 超大导致全表扫描（可按需开启）
        paginationInnerInterceptor.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}


