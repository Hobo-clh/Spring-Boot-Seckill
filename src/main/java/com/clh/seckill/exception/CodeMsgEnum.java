package com.clh.seckill.exception;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
public enum CodeMsgEnum {
    //通用
    /** 成功 */
    SUCCESS(200, "success"),
    /** 失败 */
    SERVER_ERROR(1000, "服务器异常"),

    //登录模块 5003xx

    SESSION_ERROR(500300, "Session不存在或者已经失效"),
    MOBILE_EMPTY(500301,"手机号为空" ),
    PASSWORD_EMPTY(500302,"密码为空" ),
    MOBILE_ERROR(500303,"手机号格式错误" ),
    PASSWORD_ERROR(500304,"密码错误"),
    BIND_ERROR(500306,"绑定异常"),
    MOBILE_IS_NOT_EXIST(500307,"手机号码不存在"),
    USER_IS_EMPTY(500308,"请登录后执行此操作"),
    USER_HAS_BEEN_REGISTERED(500309, "手机号已经被注册了！"),



    //邮箱模块 5004xx
    EMAIL_ERROR(500400,"邮箱错误"),
    EMAIL_OR_CODE_EMPTY(500401,"邮箱或验证码不能为空"),
    EMAIL_HAS_BEEN_USED(500402,"该邮箱已经被使用"),
    //订单模块 5005xx
    SECKILL_STOCK_EMPTY(50500,"库存不足"),
    SECKILL_REPEAT(500501,"重复秒杀"),
    ORDER_IS_EMPTY(500502,"不存在此订单"),
    REQUEST_ILLEGAL(500503, "请求非法"),
    SECKILL_FAIL(500504,"秒杀失败！"),
    VERIFY_CODE_IS_EMPTY(500505, "验证码不能为空！"),
    VERIFY_CODE_IS_ERROR(500505, "验证码输入错误！"),
    ACCESS_LIMIT_REACHED(500506,"访问太频繁了" ),
    ;

    private Integer code;
    private String msg;

    CodeMsgEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }
}
