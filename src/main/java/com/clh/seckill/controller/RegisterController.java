package com.clh.seckill.controller;

import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.dto.UserDTO;
import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.model.User;
import com.clh.seckill.rabbitmq.MQSender;
import com.clh.seckill.service.UserService;
import com.clh.seckill.util.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Slf4j
@Controller
public class RegisterController {

    @Resource
    private UserService userService;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultDTO register(UserDTO userDTO) {
        //注册
        log.info("用户注册：{}",userDTO);
        User userById = userService.findById(Long.valueOf(userDTO.getMobile()));
        if (userById != null) {
            return ResultDTO.error(CodeMsgEnum.USER_HAS_BEEN_REGISTERED);
        }
        boolean flag =  userService.register(userDTO);
        if (!flag) {
            return ResultDTO.error(CodeMsgEnum.BIND_ERROR);
        }
        return ResultDTO.success("注册成功");
    }

    @GetMapping("/image")
    public ResultDTO getImage(HttpServletResponse response) throws IOException {
        VerifyCodeUtil codeUtil = new VerifyCodeUtil();
        BufferedImage image = codeUtil.createVerifyCode(1L, 1L);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "JPEG", out);
        out.flush();
        return null;
    }

    @Resource
    private MQSender mqSender;

    // @GetMapping("/mq")
    // @ResponseBody
    // public ResultDTO mp() {
    //     String helloWorld = "hello world";
    //     mqSender.send(helloWorld);
    //     return ResultDTO.success(helloWorld);
    // }
    //
    // @GetMapping("/mq/topic")
    // @ResponseBody
    // public ResultDTO topic() {
    //     String helloWorld = "hello world";
    //     mqSender.sendTopic(helloWorld);
    //     return ResultDTO.success(helloWorld);
    // }
    //
    // @RequestMapping("/mq/fanout")
    // @ResponseBody
    // public ResultDTO<String> fanout() {
    //     String helloWorld = "hello world";
    //     mqSender.sendFanout(helloWorld);
    //     return ResultDTO.success(helloWorld);
    // }
    //
    // @RequestMapping("/mq/header")
    // @ResponseBody
    // public ResultDTO<String> header() {
    //     mqSender.sendHeader("hello,imooc");
    //     return ResultDTO.success("Hello，world");
    // }
}
