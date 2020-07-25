package com.clh.seckill.controller;

import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: LongHua
 * @date: 2020/7/21
 **/
@Controller
public class UserController {



    @GetMapping("/user_info")
    @ResponseBody
    public ResultDTO getUser(User user){
        return ResultDTO.success(user);
    }
}
