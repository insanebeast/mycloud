package com.pueeo.common.support;

import lombok.Getter;

@Getter
public enum ResultEnum {

    SERVER_ERROR(500, "服务器开小差了～请稍后再试！"),
    PARAMETER_ILLEGAL(400, "非法参数"),
    SENTINEL_ERROR(100, "Sentinel拦截"),


    USER_NOT_EXIST(10001, "用户不存在"),
    POST_NOT_EXIST(20001, "帖子不存在")

    ,
    ;

    private int code;
    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
