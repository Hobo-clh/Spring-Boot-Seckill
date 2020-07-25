package com.clh.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
public class MD5Util {

    private static final String SALT = "hobosocool";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass) {
        String str = "" + SALT.charAt(2) + SALT.charAt(1) + inputPass + SALT.charAt(5) + SALT.charAt(7);
        return md5(str);
    }

    public static String formPassToDBPass(String inputPass, String salt) {
        String str = "" + salt.charAt(0) + inputPass + salt.charAt(4);
        return md5(str);
    }

    public static String InputPassToDBPass(String inputPass, String salt) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }


    public static void main(String[] args) {
        System.out.println(InputPassToDBPass("123456", "socool"));
    }

}
