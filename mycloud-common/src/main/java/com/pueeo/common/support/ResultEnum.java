package com.pueeo.common.support;

import lombok.Getter;

@Getter
public enum ResultEnum {

    SERVER_ERROR(500, "服务器开小差了～请稍后再试！"),
    PARAMETER_ILLEGAL(4000, "非法参数")

    ,
    ;

    private int code;
    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
