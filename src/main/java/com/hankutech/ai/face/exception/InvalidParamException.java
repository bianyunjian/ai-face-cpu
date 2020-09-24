package com.hankutech.ai.face.exception;


/**
 * 无效参数异常
 *
 * @author ZhangXi
 */
public class InvalidParamException extends Exception {

    private int errorCode;

    public InvalidParamException(String message) {
        super(message);
    }

    public InvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }


    public InvalidParamException with(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
