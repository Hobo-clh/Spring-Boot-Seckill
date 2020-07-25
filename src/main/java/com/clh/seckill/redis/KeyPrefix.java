package com.clh.seckill.redis;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
