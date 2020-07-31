package com.clh.seckill.util;

import com.clh.seckill.redis.SeckillKey;
import com.clh.seckill.service.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author: LongHua
 * @date: 2020/7/31
 **/
@Component
public class VerifyCodeUtil {

    @Resource
    private RedisService redisService;
    /**
     * 创建随机验证码图片，并将其计算结果放至redis中
     *
     * @param goodsId 秒杀商品id
     * @param userId  用户
     * @return 返回生成的验证码图片
     */
    public BufferedImage createVerifyCode(Long goodsId, Long userId) {
        if (userId <= 0 || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // 设置背景色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // 创建一个随机实例来生成代码
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // 生成随机码
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getVerifyCode, goodsId + "_" + userId, rnd);

        //输出图片
        return image;
    }

    /**
     * 对数学公式进行运算
     *
     * @param exp 数学公式的字符串
     * @return 公式的计算结果
     */
    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 生成验证码的数学公式字符串
     *
     * @param rdm 随机数流
     * @return 返回 （数 运算符 数 运算符 数）格式的字符串
     */
    private String generateVerifyCode(Random rdm) {
        char[] ops = new char[]{'+', '-', '*'};
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }
}
