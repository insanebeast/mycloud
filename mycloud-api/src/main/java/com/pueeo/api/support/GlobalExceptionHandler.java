package com.pueeo.api.support;

import com.pueeo.common.exception.RpcServiceException;
import com.pueeo.common.support.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RpcServiceException.class)
    public ApiResult handleException(RpcServiceException exception) {
        log.error(String.format("全局异常[SerivceException]:%s--%s",exception.getCode(), exception.getMessage()), exception);
        return ApiResult.fail(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleException(RuntimeException exception) {
        log.error(String.format("全局异常[RuntimeException]:%s", exception.getMessage()), exception);
        return ApiResult.fail(exception.getMessage());
    }
}
