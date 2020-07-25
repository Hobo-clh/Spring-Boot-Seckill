package com.clh.seckill.exception;

import com.clh.seckill.dto.ResultDTO;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResultDTO<String> exceptionHandler(HttpServletRequest request,Exception e){

        if (e instanceof GlobleException) {
            GlobleException e1 = (GlobleException) e;
            CodeMsgEnum codeMsg = e1.getCodeMsg();
            return ResultDTO.error(codeMsg);

        }else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String message = error.getDefaultMessage();
            return ResultDTO.error(CodeMsgEnum.BIND_ERROR,message);

        } else {
            return ResultDTO.error(CodeMsgEnum.SERVER_ERROR);
        }
    }
}
