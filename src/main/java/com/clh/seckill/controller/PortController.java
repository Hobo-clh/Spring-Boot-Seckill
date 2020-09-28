package com.clh.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: LongHua
 * @date: 2020/8/22
 **/
@RestController
public class PortController {
    @Autowired
    Environment environment;


    @GetMapping("/port")
    public String getPort(HttpServletResponse response, HttpServletRequest request) {
        response.addCookie(new Cookie("czw666", "阿里CTO"));
        response.addCookie(new Cookie("czwddd", "阿里CEO"));
        HttpSession session = request.getSession();
        return environment.getProperty("local.server.port");
    }

}
