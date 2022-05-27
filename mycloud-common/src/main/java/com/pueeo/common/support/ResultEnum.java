package com.pueeo.common.support;

import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.utils.Assert;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
public enum ResultEnum implements Assert {
    CLIENT_AUTHENTICATION_FAILED(1001,"客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR(1002,"用户名或密码错误"),
    PHONE_OR_CODE_ERROR(1002, "手机号或验证码错误"),
    UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式"),
    INVALID_TOKEN(1004,"无效的token"),
    INVALID_SCOPE(1005,"无效的scope"),
    INVALID_REQUEST(1006, "无效的请求"),
    FORBID_REQUEST(1007, "禁止访问"),

    NO_PERMISSION(1005,"无权限访问"),
    USER_ACCOUNT_NOT_EXIST(1006,"账号不存在"),
    SYSTEM_ERROR(500, "系统错误"),


    FREQUENT_REQUEST(429, "操作太快了，休息一会儿～"),
    SERVER_ERROR(500, "服务器开小差了～请稍后再试！"),
    PARAMETER_ILLEGAL(400, "非法参数"),
    SENTINEL_ERROR(100, "Sentinel拦截"),


    USER_NOT_EXIST(10001, "用户不存在"),
    USER_ACCOUNT_EXIST(10002, "账号已存在"),
    POST_NOT_EXIST(20001, "帖子不存在"),

    TEST_ERROR(12345, "{0}不能为{1}")

    ,
    ;

    private int code;
    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public BusinessException newException(Object... args) {
        if (args.length == 0){
            return new BusinessException(code, message);
        }
        String msg = MessageFormat.format(message, args);
        return new BusinessException(code, msg);
    }
}
