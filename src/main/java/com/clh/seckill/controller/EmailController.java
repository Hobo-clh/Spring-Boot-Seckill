package com.clh.seckill.controller;

import com.clh.seckill.access.AccessLimit;
import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.log.AopLog;
import com.clh.seckill.model.User;
import com.clh.seckill.service.EmailService;
import com.clh.seckill.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @author: LongHua
 * @date: 2020/8/19
 **/
@RestController
public class EmailController {

    private Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

    @Resource
    private EmailService emailService;
    @Resource
    private UserService userService;

    @AccessLimit(seconds = 60, maxCount = 2)
    @PostMapping("/send_email")
    public ResultDTO sendEmail(String email, User user) {
        if (!pattern.matcher(email).matches()) {
            return ResultDTO.error(CodeMsgEnum.EMAIL_ERROR);
        }
        emailService.sendCode(email, user);
        return ResultDTO.success();
    }

    @AopLog()
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/verify_code")
    public ResultDTO verifyCode(String email, User user, String code, HttpServletRequest request) {

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)) {
            return ResultDTO.error(CodeMsgEnum.EMAIL_OR_CODE_EMPTY);
        }
        if (!pattern.matcher(email).matches()) {
            return ResultDTO.error(CodeMsgEnum.EMAIL_ERROR);
        }
        if (!emailService.verifyCode(email, user, code)) {
            return ResultDTO.error(CodeMsgEnum.VERIFY_CODE_IS_ERROR);
        }
        //验证成功后将其绑定
        user.setEmail(email);
        try {
            userService.updateUser(user, request);
        } catch (DuplicateKeyException e) {
            //如果出现
            return ResultDTO.error(CodeMsgEnum.EMAIL_HAS_BEEN_USED);
        }
        return ResultDTO.success("绑定成功");
    }
}
