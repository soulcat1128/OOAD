package com.bookmanager.bms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //是否發送Cookie
                .allowCredentials(true)
                //設置放行哪些原始域   SpringBoot2.4.4下低版本使用.allowedOrigins("*")
                .allowedOriginPatterns("*")
                //放行哪些請求方式
                .allowedMethods("GET","POST","PUT","DELETE")
                //.allowedMethods("*") //或者放行全部
                //暴露哪些原始請求頭部資訊
                .allowedHeaders("*");
    }
}
