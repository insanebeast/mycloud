package com.pueeo.common.exception.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.support.ApiResult;
import com.pueeo.common.support.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResult handleException(BusinessException exception) {
        log.error(String.format("全局异常处理[ServiceException]:%s--%s",exception.getCode(), exception.getMessage()), exception);
        return ApiResult.fail(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleException(RuntimeException exception) {
        log.error(String.format("全局异常处理[RuntimeException]:%s", exception.getMessage()), exception);
        return ApiResult.fail(ResultEnum.SERVER_ERROR);
    }

    @ExceptionHandler(BlockException.class)
    public ApiResult handleException(BlockException exception) {
        log.error(String.format("全局异常处理[%s]:%s", exception.getClass().getSimpleName(), exception.getMessage()), exception);
        return ApiResult.fail(ResultEnum.SENTINEL_ERROR);
    }
}
