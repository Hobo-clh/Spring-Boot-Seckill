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
    MOBILE_NOT_EXIST(500305,"手机号不存在"),
    BIND_ERROR(500306,"绑定异常"),

    MOBILE_IS_NOT_EXIST(500307,"手机号码不存在"),

    //商品模块 400xx

    //订单模块 500xx
    SECKILL_STOCK_EMPTY(500500,"库存不足"),
    SECKILL_REPEAT(5000501,"重复秒杀"),
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
