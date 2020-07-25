package com.clh.seckill.dto;

import com.clh.seckill.exception.CodeMsgEnum;
import lombok.Data;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Data
public class ResultDTO<T> {

    private Integer code;
    private String msg;
    private T Data;

    private ResultDTO(CodeMsgEnum codeMsg) {
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    public static <T> ResultDTO<T> success(T data){
        ResultDTO<T> resultDto = new ResultDTO<T>(CodeMsgEnum.SUCCESS);
        resultDto.setData(data);
        return resultDto;
    }

    public static ResultDTO error(CodeMsgEnum codeMsg){
        return new ResultDTO(codeMsg);
    }

    public static ResultDTO error(CodeMsgEnum codeMsg,String msg){
        ResultDTO resultDTO = new ResultDTO(codeMsg);
        resultDTO.setMsg(resultDTO.getMsg() + ":" + msg);
        return resultDTO;
    }
}
