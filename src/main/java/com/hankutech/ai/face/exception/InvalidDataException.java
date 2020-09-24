package com.hankutech.ai.face.exception;

/**
 * 无效数据异常
 *
 * @author ZhangXi
 */
public class InvalidDataException extends Exception {

    private int errorCode;

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDataException with(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
