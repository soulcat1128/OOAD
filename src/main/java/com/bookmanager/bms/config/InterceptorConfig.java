package com.bookmanager.bms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  //表明是配置類
public class InterceptorConfig implements WebMvcConfigurer {

    // 所有的頁面和請求
    private static final String[] all = {
            "/**/*.html",
            "/bookInfo/**",
            "/bookType/**",
            "/borrow/**",
            "/update/**",
            "/user/**"
    };

    // 與登入和註冊相關的界面和請求
    public static final String[] aboutLogin = {
            "/",
            "/index.html",
            "/register.html",
            "/user/login",
            "/user/register"
    };

    // 與讀者相關的頁面和請求
    public static final String[] aboutReader = {
            "/reader/**/*.html",    // 讀者頁面
            "/*/reader/**",         // 讀者請求
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注意，因為瀏覽器快取問題，導致不一定能攔截頁面，但是功能請求肯訂做不了，所以也不影響安全性。

        /**
         * 用戶攔截器思路：
         * 1. 攔截所有的頁面和請求
         * 2. 排除與登入和註冊相關的
         */
        /*registry.addInterceptor(new UserInterceptor())
                .addPathPatterns(all)
                .excludePathPatterns(aboutLogin);*/

        /**
         * 讀者攔截器思路：
         * 1. 攔截所有頁面和請求
         * 2. 排除與登入相關的頁面和請求
         * 3. 排除與讀者有關的頁面和請求
         */
        /*registry.addInterceptor(new ReaderInterceptor())
                .addPathPatterns(all)
                .excludePathPatterns(aboutLogin)
                .excludePathPatterns(aboutReader);*/
    }
}
