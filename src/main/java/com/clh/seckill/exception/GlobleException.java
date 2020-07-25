package com.clh.seckill.exception;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
public class GlobleException extends RuntimeException {

    private CodeMsgEnum codeMsg;

    public GlobleException(CodeMsgEnum codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsgEnum getCodeMsg() {
        return codeMsg;
    }
}
