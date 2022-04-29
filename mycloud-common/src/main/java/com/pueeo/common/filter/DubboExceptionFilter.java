package com.pueeo.common.filter;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.pueeo.common.exception.ServiceException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.filter.ExceptionFilter;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * dubbo服务间默认通过ExceptionFilter包装成RuntimeException返回
 * 这里自定义拦截，指定ServiceException异常直接抛出
 *
 * tips：
 * 针对被@SentinelResource标记的资源
 * 若blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 BlockException 时只会进入 blockHandler 处理逻辑
 * 若未配置 blockHandler、fallback 和 defaultFallback，则被限流降级时会将 BlockException 直接抛出
 * 若方法本身未定义 throws BlockException 则会被JVM包装一层 UndeclaredThrowableException
 */
@Activate(group = CommonConstants.PROVIDER)
public class DubboExceptionFilter extends ExceptionFilter {

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            Throwable exception = appResponse.getException();
            //ServiceException直接抛出
            if (exception instanceof ServiceException){
                return;
            }
            //触发限流降级时，将BlockException包装成ServiceException抛出（由于BlockException未实现Seralizable，在Dubbo序列化时会报错）
            if (exception instanceof BlockException){
                setBlockException(appResponse, exception);
                return;
            }
            //触发限流降级时，BlockException若未通过throws显式抛出，会被JVM包装成UndeclaredThrowableException，这里解析并包装成ServiceException
            if (exception instanceof UndeclaredThrowableException) {
                Throwable throwable = ((UndeclaredThrowableException) exception).getUndeclaredThrowable();
                setBlockException(appResponse, throwable);
                return;
            }
        }
        super.onResponse(appResponse, invoker, invocation);
    }

    private void setBlockException(Result appResponse, Throwable exception) {
        if (exception instanceof FlowException) {
            appResponse.setException(new ServiceException(500, "Sentinel触发【流量控制】"));
        }
        if (exception instanceof AuthorityException) {
            appResponse.setException(new ServiceException(500, "Sentinel触发【黑白名单控制】"));
        }
        if (exception instanceof SystemBlockException) {
            appResponse.setException(new ServiceException(500, "Sentinel触发【系统自适应限流】"));
        }
        if (exception instanceof ParamFlowException) {
            appResponse.setException(new ServiceException(500, "Sentinel触发【热点参数限流】"));
        }
        if (exception instanceof DegradeException) {
            appResponse.setException(new ServiceException(500, "Sentinel触发【熔断降级】"));
        }
    }
}
