package com.clh.seckill.service;

import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.exception.GlobleException;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.User;
import com.clh.seckill.redis.UserKey;
import com.clh.seckill.util.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/8/19
 **/
@Service
public class EmailService {

    @Resource
    private EmailUtil emailUtil;
    @Resource
    private RedisService redisService;

    public void sendCode(String email, User user) {
        String title = "【秒杀小商城】绑定邮箱验证码";
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        String content = "【秒杀小商城】欢迎您绑定邮箱账号，您的验证码为:" + code + "，请在5分钟内完成绑定。";
        redisService.set(UserKey.byEmail, user.getId() + "_" + email, code);
        emailUtil.sendEmail(email, title, content);

    }

    public boolean verifyCode(String email, User user, String code) {
        if (StringUtils.isEmpty(email) || user == null || StringUtils.isEmpty(code)) {
            throw new GlobleException(CodeMsgEnum.VERIFY_CODE_IS_ERROR);
        }
        String dbCode = redisService.get(UserKey.byEmail, user.getId() + "_" + email, String.class);
        boolean a = code.equals(dbCode);
        if (a) {
            // 成功后删除验证码
            redisService.delete(UserKey.byEmail, user.getId() + "_" + email);
        }
        return a;
    }

    public void sendSeckillSucess(String email, GoodsExtend goodsExtend) {
        String title = "【秒杀小商城】恭喜您，秒杀成功！";
        Double seckillPrice = goodsExtend.getSeckillPrice();
        Double goodsPrice = goodsExtend.getGoodsPrice();
        String goodsName = goodsExtend.getGoodsName();
        // 【秒杀小商城】恭喜您，以 {seckillPrice} 的秒杀价，原价【 {goodsPrice} 】，成功秒杀到 {goodsName}
        StringBuilder builder = new StringBuilder("【秒杀小商城】恭喜您，以")
                .append(seckillPrice).append("的秒杀价，原价【").append(goodsPrice + "】")
                .append("成功秒杀到").append(goodsName).append("，快去看看吧!");
        emailUtil.sendEmail(email, title, builder.toString());
    }
}
