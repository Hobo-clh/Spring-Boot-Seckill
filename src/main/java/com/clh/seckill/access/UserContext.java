package com.clh.seckill.access;

import com.clh.seckill.model.User;

/**
 * @author: LongHua
 * @date: 2020/7/30
 **/
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }


}
