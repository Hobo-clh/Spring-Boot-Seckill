package com.clh.seckill.redis;

import lombok.AllArgsConstructor;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;


    public BasePrefix(String prefix) {
        // 0 代表永不过期
        this(0, prefix);
    }


    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+ ":" + prefix;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }
}
