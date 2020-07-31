package com.clh.seckill.redis;

/**
 * @author: LongHua
 * @date: 2020/7/30
 **/
public class AccessKey extends BasePrefix {
    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    private static final String ACCESS = "access";
    public static final AccessKey access = new AccessKey(10, ACCESS);

    public static AccessKey withExpire(int expire) {
        return new AccessKey(expire, ACCESS);
    }
}
