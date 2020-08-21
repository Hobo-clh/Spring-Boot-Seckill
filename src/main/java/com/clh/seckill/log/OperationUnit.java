package com.clh.seckill.log;

/**
 * @author: LongHua
 * @date: 2020/8/20
 **/
public enum OperationUnit {
    /**
     * 操作单元
     */
    UNKNOWN("unknown"),
    USER("user"),
    EMPLOYEE("employee"),
    Redis("redis");

    private String value;

    OperationUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
