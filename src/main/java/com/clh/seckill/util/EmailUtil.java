package com.clh.seckill.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/8/19
 **/
@Component
public class EmailUtil {
    @Resource
    private JavaMailSender mailSender;
    @Value("${customize.sender}")
    private String sender;

    /**
     * @param receiver 接收者的邮箱
     * @param title    邮件标题
     * @param content  邮件内容
     * @throws MailException 发送异常
     */
    public void sendEmail(String receiver, String title, String content) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(receiver);
        message.setFrom(sender);
        mailSender.send(message);
    }
}
