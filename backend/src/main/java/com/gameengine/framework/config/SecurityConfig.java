package com.gameengine.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gameengine.framework.security.filter.JwtAuthenticationTokenFilter;

/**
 * Spring Security 配置
 * 
 * @author GameEngine
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF
            .csrf().disable()
            // 使用无状态会话
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 禁用 Spring Security 默认的 logout 处理
            .logout().disable()
            // 配置请求授权
            .authorizeRequests()
                // 允许访问登录接口
                .antMatchers("/login", "/logout", "/getInfo", "/captchaImage", "/register", "/forgotPassword").permitAll()
                // 第三方登录
                .antMatchers("/oauth/**").permitAll()
                // 微信绑定
                .antMatchers("/weixinbind").permitAll()
                // 允许访问个人资料接口（需要认证）
                .antMatchers("/profile/**").authenticated()
                // 允许访问 Swagger 相关路径（SpringDoc OpenAPI）
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/doc.html", "/webjars/**", 
                             "/v3/api-docs/**", "/swagger-resources/**", "/favicon.ico").permitAll()
                // 允许访问 Druid 监控
                .antMatchers("/druid/**").permitAll()
                // 允许访问测试接口
                .antMatchers("/test/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            .and()
            // 添加JWT filter
            .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            // 禁用 HTTP Basic 认证
            .httpBasic().disable();
    }
}

