package com.hankutech.ai.face.constant;

import com.hankutech.ai.face.pojo.response.BaseResponse;

/**
 * 响应状态枚举类
 * 用来匹配 {@link BaseResponse}的status属性值
 *

 */
public enum ResponseStatus {

    /**
     * 响应状态
     */
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    UNAUTHORIZED("UNAUTHORIZED"),
    DENIED("DENIED");

    private String enumValue;

    ResponseStatus(String enumValue) {
        this.enumValue = enumValue;
    }


}
