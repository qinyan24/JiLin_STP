package com.stp.cookieInterceptor;

import com.stp.domain.User;
import com.stp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieInterceptor implements HandlerInterceptor {
    // 目标 Cookie 名称
    private static final String AUTH_COOKIE_NAME = "userId";
    // 登录页路径
    private static final String LOGIN_PAGE = "/login";

    @Autowired
    private UserService userService;  // 引入 UserService 来验证用户信息

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 打印请求路径，检查是否有意外的路径被拦截
        System.out.println("Request URL: " + request.getRequestURI());
        // 获取所有 Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                    // 如果找到了 userId，我们根据 userId 从数据库验证用户是否有效
                    int userId = Integer.parseInt(cookie.getValue());  // 获取 userId
                    User user = userService.getUserById(userId);  // 查询用户
                    if (user != null) {
                        // 用户有效，放行请求
                        request.setAttribute("userId", userId);
                        return true;  // 放行请求
                    } else {
                        // 用户不存在或无效，跳转到登录页
                        response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
                        return false;  // 拦截请求
                    }
                }
            }
        }

        // 未找到 Cookie，跳转到登录页
        response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
        return false; // 拦截请求
    }
}
