package com.clh.seckill.redis;

/**
 * @author: LongHua
 * @date: 2020/7/23
 **/
public class SecKillOrderKey extends BasePrefix {

    private SecKillOrderKey(int expireSecond, String prefix) {
        super(expireSecond,prefix);
    }

    private static final int THREE_HOURS = 60 * 60 * 6;
    public static final SecKillOrderKey getUIdGoodsId = new SecKillOrderKey(THREE_HOURS, "uIdGoodsId");

}
