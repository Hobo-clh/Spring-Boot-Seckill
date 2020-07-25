package com.clh.seckill.util;

import java.util.UUID;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
