package com.clh.seckill.config;

import com.clh.seckill.model.User;
import com.clh.seckill.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: LongHua
 * @date: 2020/8/19
 **/
@Component
public class CookieInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = userService.getUser(request, response);
        if (user != null) {
            request.getSession().setAttribute("user", user);
        }
        return true;
    }
}
