package com.hankutech.ai.face.pojo.response;

import com.hankutech.ai.face.constant.ErrorCode;
import com.hankutech.ai.face.support.BaseUtils;
import lombok.Data;

/**
 * HTTP基础响应类
 *
 * @author ZhangXi
 */
@Data
public class BaseResponse<T> {

    public BaseResponse() {
    this.recordId= BaseUtils.generateRecordId();
    }

    private String recordId;
    private int errId;
    private T data;
    private String errMsg = "";


    public void success() {
        this.errId = ErrorCode.DEFAULT_NO_ERROR.getValue();
    }

    public void success(String message) {
        success();
        this.errMsg = message;
    }

    public void success(String message, T data) {
        success(message);
        this.data = data;
    }

    public void fail() {
        this.errId = ErrorCode.INNER_ERROR.getValue();
    }

    public void fail(String message) {
        fail();
        this.errMsg = message;
    }

    public void fail(String message, int errorCode) {
        fail(message);

        this.errId = errorCode;

    }


}
