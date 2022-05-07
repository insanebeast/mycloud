package com.pueeo.common.exception.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.pueeo.common.exception.ServiceException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义sentinel异常处理器
 * 默认情况下，BlockExceptionHandler 有一个默认的 DefaultBlockExceptionHandler 实现类，返回Block字符串提示
 * 这里自定义异常处理器，将异常直接抛出，交由SpringMVC的全局异常处理器处理
 *
 * PS：此类只针对于直接调用接口层。当存在网关层时，可以直接在网关层处理，这个就不需要了
 *
 */
@Component
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) {
        if (e instanceof FlowException){
            throw new ServiceException(300, "Sentinel触发【流量控制】");
        }
    }
}
