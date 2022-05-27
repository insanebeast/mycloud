package com.pueeo.common.exception.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.support.ApiResult;
import com.pueeo.common.support.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResult handleException(BusinessException exception) {
        log.error(String.format("全局异常处理[BusinessException]:%s--%s",exception.getCode(), exception.getMessage()), exception);
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

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public ApiResult handleValidatedException(Exception exception) {
        log.error(String.format("全局异常处理[ValidatedException]:%s", exception.getMessage()), exception);
        ApiResult resp = ApiResult.fail(ResultEnum.PARAMETER_ILLEGAL);

        if (exception instanceof BindException) {
            // body或@RequestBody传参
            BindException ex = (BindException) exception;
            String msg = ex.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("；"));
            resp = ApiResult.fail(ResultEnum.PARAMETER_ILLEGAL.getCode(), msg);
        } else if (exception instanceof ConstraintViolationException) {
            // RequestParam或PathVariable传参
            ConstraintViolationException ex = (ConstraintViolationException) exception;
            String msg = ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            resp = ApiResult.fail(ResultEnum.PARAMETER_ILLEGAL.getCode(), msg);
        }
        return resp;
    }
}
