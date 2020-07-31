package com.clh.seckill.redis;

/**
 * @author: LongHua
 * @date: 2020/7/30
 **/
public class SeckillKey extends BasePrefix {

    private SeckillKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    private static final Integer EXPIRE_TIME = 60;

    public static final SeckillKey isGoodsOver = new SeckillKey(EXPIRE_TIME,"isGoodsOver");
    public static final SeckillKey getSeckillPath = new SeckillKey(EXPIRE_TIME,"seckillPath ");
    public static final SeckillKey getVerifyCode = new SeckillKey(EXPIRE_TIME * 5,"verifyCode ");
}
