package com.hyh.blog.config;

import com.hyh.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huyuhui
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器配置
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                //未登录时不能评论文章，拦截添加评论的接口
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }
}
