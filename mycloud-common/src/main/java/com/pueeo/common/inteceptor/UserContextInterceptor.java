package com.pueeo.common.inteceptor;

import com.pueeo.common.constant.SysConstant;
import com.pueeo.common.support.LoginUser;
import com.pueeo.common.support.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截请求，获取用户信息填充UserContext
 * 主要用于服务远程调用时共享UserContext
 *
 */
@Slf4j
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginUser loginUser = (LoginUser)request.getAttribute(SysConstant.USER_CONTEXT_REQUEST_ATTRIBUTE);
        UserContext.set(loginUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
 
}