package com.pueeo.common.filter;

import com.pueeo.common.exception.RpcServiceException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.filter.ExceptionFilter;
import org.apache.dubbo.rpc.service.GenericService;

@Activate(group = CommonConstants.PROVIDER)
public class DubboExceptionFilter extends ExceptionFilter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = invoker.invoke(invocation);
        if (!result.hasException() || GenericService.class == invoker.getInterface()){
            return result;
        }
        Throwable exception = result.getException();
        if (exception instanceof RpcServiceException){
            return result;
        }
        return super.invoke(invoker, invocation);
    }
}
