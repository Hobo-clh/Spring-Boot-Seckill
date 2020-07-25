package com.clh.seckill.controller;

import com.clh.seckill.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Controller
public class RegisterController {

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(UserDTO userDTO){
        //注册
        return null;
    }
}
