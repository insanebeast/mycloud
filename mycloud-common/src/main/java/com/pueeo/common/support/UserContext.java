package com.pueeo.common.support;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 在服务间传递的用户信息
 */

public class UserContext {

    private static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    public static LoginUser get(){
        return threadLocal.get();
    }

    public static void set(LoginUser loginUser){
        threadLocal.set(loginUser);
    }

    public static void clear(){
        threadLocal.remove();
    }


}


