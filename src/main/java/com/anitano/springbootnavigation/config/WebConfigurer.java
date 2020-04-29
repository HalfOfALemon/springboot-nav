package com.anitano.springbootnavigation.config;

import com.anitano.springbootnavigation.component.AdminHandlerInterceptor;
import com.anitano.springbootnavigation.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: WebConfigurer
 * @Author: 杨11352
 * @Date: 2020/4/12 13:08
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    /*解决拦截器无法注入bean*/
    @Bean
    public AdminHandlerInterceptor adminHandlerInterceptor(){
        return new AdminHandlerInterceptor();
    }
    @Bean
    public LoginHandlerInterceptor loginHandlerInterceptor(){
        return new LoginHandlerInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*注册管理员拦截器*/
        registry.addInterceptor(adminHandlerInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
        /*注普通用户拦截器*/
        registry.addInterceptor(loginHandlerInterceptor())
                .addPathPatterns("/customer/**");
    }
}
