package com.clh.seckill.controller;

import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.redis.UserKey;
import com.clh.seckill.service.RedisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/7/17
 **/
@Controller
public class TestController {

    @Resource
    private RedisService redisService;

    // @GetMapping("/test")
    // @ResponseBody
    // public ResultDTO<String> test() {
    //     // redisService.set(UserKey.getById,"--1","hobo666666");
    //     // String key1 = redisService.get(UserKey.getById,"--1", String.class);
    //     // return ResultDTO.success(key1);
    // }

    @PutMapping("/put")
    @ResponseBody
    public String putTest(){
        return "put成功";
    }

    @GetMapping("/test2")
    public String goTest(Model model) {

        model.addAttribute("name", "hobo");
        return "test";
    }

}
