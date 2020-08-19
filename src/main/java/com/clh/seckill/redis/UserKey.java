package com.clh.seckill.redis;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
public class UserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 60 * 60 * 24 * 30;

    private UserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static UserKey getById = new UserKey(0,"id");
    public static UserKey getByName = new UserKey(TOKEN_EXPIRE,"name");
    public static UserKey token = new UserKey(TOKEN_EXPIRE,"token");

    public static UserKey byEmail = new UserKey(5 * 60, "email");
}
