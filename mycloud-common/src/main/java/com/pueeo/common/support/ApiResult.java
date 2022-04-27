package com.pueeo.common.support;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiResult {

    private int code;
    private String message;
    private Object data;
    private long timestamp = System.currentTimeMillis();

    public ApiResult() {
        this.code = 200;
        this.message = "success";
    }

    public ApiResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ApiResult success(Object data){
        return new ApiResult().setData(data);
    }

    public static ApiResult success(){
        return new ApiResult();
    }

    public static ApiResult fail(int code, String message){
        return new ApiResult(code, message);
    }

    public static ApiResult fail(ResultEnum resultEnum){
        return new ApiResult(resultEnum.getCode(), resultEnum.getMessage());
    }

    public static ApiResult fail(String message){
        return new ApiResult(ResultEnum.SERVER_ERROR.getCode(), message);
    }

}
