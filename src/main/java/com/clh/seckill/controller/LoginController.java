package com.clh.seckill.controller;

import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.dto.UserDTO;
import com.clh.seckill.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultDTO login(@Valid UserDTO userDTO, HttpServletResponse response) {
        log.info(userDTO.toString());
        String token = userService.login(userDTO, response);
        return ResultDTO.success(token);
    }
}
