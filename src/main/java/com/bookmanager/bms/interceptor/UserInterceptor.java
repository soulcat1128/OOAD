package com.bookmanager.bms.interceptor;

import com.bookmanager.bms.model.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 用戶攔截器，攔截未登錄不能訪問的請求
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("userObj");

        if(user == null) {  //沒有登錄
            System.out.println("沒有登錄!不能訪問!");
            // 重定向到登錄界面
            response.sendRedirect(request.getContextPath() + "/index.html");
            return false;
        }

        return true;    //放行
    }

}
