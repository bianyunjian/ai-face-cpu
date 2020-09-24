package com.hankutech.ai.face.config;

import com.hankutech.ai.face.constant.ErrorCode;
import com.hankutech.ai.face.exception.InvalidDataException;
import com.hankutech.ai.face.exception.InvalidParamException;
import com.hankutech.ai.face.pojo.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author ZhangXi
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse handleException(Exception e) {
        log.error("处理未知异常", e);
        BaseResponse response = new BaseResponse();
        String message = "发生未知错误";
        response.fail(message, ErrorCode.INNER_ERROR.getValue());
        return response;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("处理参数校验异常", e);
        BaseResponse response = new BaseResponse();
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        String[] infos = null;
        String message = null;
        if (null != errors && errors.size() > 0) {
            FieldError error = errors.get(0);
            Object rejectedValue = error.getRejectedValue();
            infos = new String[]{
                    error.getField(),
                    rejectedValue == null ? null : rejectedValue.toString(),
                    error.getDefaultMessage()
            };
            message = MessageFormat.format("参数：{0}={1} 校验失败，原因：{2}", infos[0], infos[1], infos[2]);
        }
        response.fail(message, ErrorCode.PARAM_INVALID.getValue());
        return response;
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseBody
    public BaseResponse handleInvalidDataException(InvalidDataException e) {
        log.error("处理数据操作无效异常", e);
        BaseResponse response = new BaseResponse();
        response.fail(e.getMessage(), e.getErrorCode());
        return response;
    }

    @ExceptionHandler(InvalidParamException.class)
    @ResponseBody
    public BaseResponse handleInvalidParamException(InvalidParamException e) {
        log.error("处理自定义参数校验异常", e);
        BaseResponse response = new BaseResponse();
        response.fail(e.getMessage(), e.getErrorCode());
        return response;
    }

}
