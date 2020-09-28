package com.clh.seckill.access;

import com.alibaba.fastjson.JSON;
import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.model.User;
import com.clh.seckill.redis.AccessKey;
import com.clh.seckill.service.RedisService;
import com.clh.seckill.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: LongHua
 * @date: 2020/7/30
 **/
@Slf4j
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;
    @Resource
    private RedisService redisService;

    /**
     * 针对AccessLimit注解类的拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            User user = userService.getUser(request, response);
            UserContext.setUser(user);
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response, CodeMsgEnum.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getId();
            }
            //接口限流
            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);
            if (count == null) {
                redisService.set(ak, key, 1);
            } else if (count < maxCount) {
                redisService.incr(ak, key);
            } else {
                //渲染
                render(response, CodeMsgEnum.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    /**
     * 渲染至response
     *
     * @param response 响应response
     * @param cm       CodeMsgEnum枚举错误信息
     * @throws IOException 向外抛出IOException
     */
    private void render(HttpServletResponse response, CodeMsgEnum cm) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String str = JSON.toJSONString(ResultDTO.error(cm));
            out.write(str);
            out.flush();
        }

        // try (OutputStream out = response.getOutputStream()) {
        //     String str = JSON.toJSONString(ResultDTO.error(cm));
        //     out.write(str.getBytes("UTF-8"));
        //     out.flush();
        // }

    }
}
