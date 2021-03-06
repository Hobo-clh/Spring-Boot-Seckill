package com.clh.seckill.controller;

import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.dto.UserDTO;
import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.model.User;
import com.clh.seckill.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Slf4j
@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @GetMapping("/to_login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/person")
    public String goEmail() {
        return "bind_email";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultDTO login(@Valid UserDTO userDTO, HttpServletResponse response, HttpServletRequest request) {
        log.info(userDTO.toString());
        String token = userService.login(userDTO, response, request);
        return ResultDTO.success(token);
    }

    @GetMapping("/logout")
    @ResponseBody
    public ResultDTO logout(HttpServletResponse response, HttpServletRequest request) {
        response.addCookie(new Cookie(UserService.COOKIE_NAME_TOKEN, null));
        request.getSession().removeAttribute("user");
        return ResultDTO.success();
    }


    @GetMapping("/token")
    @ResponseBody
    public ResultDTO getUserByToken(User user) {
        return user == null ? ResultDTO.error(CodeMsgEnum.SESSION_ERROR) : ResultDTO.success(user);
    }
}
