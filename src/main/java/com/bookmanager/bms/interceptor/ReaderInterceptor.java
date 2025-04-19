package com.bookmanager.bms.interceptor;

import com.bookmanager.bms.model.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 讀者攔截器：攔截讀者不能訪問的請求
public class ReaderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("userObj");

        // 根據鏈式攔截關係，到這個攔截器肯定是登錄過的。
        if(user.getIsadmin() == 0) {    //如果是用戶，則攔截
            System.out.println("讀者不能進管理員界面!");
            // 重定向到登錄界面
            response.sendRedirect(request.getContextPath() + "/index.html");
            return false;
        }
        return true;    //放行
    }
}
