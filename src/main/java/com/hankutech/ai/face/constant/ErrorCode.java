package com.hankutech.ai.face.constant;


public enum ErrorCode {

    /**
     * 系统错误码
     */
    DEFAULT_NO_ERROR(0, "默认无错误"),
    INNER_ERROR(100001, "系统内部错误"),
    PARAM_INVALID(100002, "参数校验无效");


    private Integer errorCode;
    private String desc;

    ErrorCode(Integer errorCode, String desc) {
        this.errorCode = errorCode;
        this.desc = desc;
    }
    public  int getValue(){
        return  errorCode;
    }
}


