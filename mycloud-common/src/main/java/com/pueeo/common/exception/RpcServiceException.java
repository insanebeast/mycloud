package com.pueeo.common.exception;

import com.pueeo.common.support.ResultEnum;

/**
 * 服务调用异常
 */
public class RpcServiceException extends RuntimeException{

    private int code;

    public RpcServiceException() { // 创建默认构造方法，用于反序列化的场景。
    }

    public RpcServiceException(int code, String message){
        super(message);
        this.code = code;
    }

    public RpcServiceException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}
