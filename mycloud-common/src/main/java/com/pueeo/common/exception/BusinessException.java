package com.pueeo.common.exception;

import com.pueeo.common.support.ResultEnum;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException{

    private int code;

    public BusinessException() { // 创建默认构造方法，用于反序列化的场景。
    }

    public BusinessException(int code, String message){
        super(message);
        this.code = code;
    }

    public BusinessException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}
