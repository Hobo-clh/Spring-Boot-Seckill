package com.clh.seckill.log;

/**
 * @author: LongHua
 * @date: 2020/8/20
 **/
public enum OperationType {
    /**
     * 操作类型
     */
    UNKNOWN("unknown"),
    DELETE("delete"),
    SELECT("select"),
    UPDATE("update"),
    INSERT("insert");

    private String value;

    public String getValue() {
        return value;
    }

    OperationType(String s) {
        this.value = s;
    }

}
