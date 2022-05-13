package com.pueeo.oauth.exception;

import com.pueeo.common.support.ResultEnum;

public class BadCredentialsException extends org.springframework.security.authentication.BadCredentialsException {

    private ResultEnum resultEnum;

    public ResultEnum getResultEnum() {
        return resultEnum;
    }

    public BadCredentialsException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
    }

    public BadCredentialsException(String msg) {
        super(msg);
    }
}
