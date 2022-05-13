package com.pueeo.dubbo.filter;

import com.alibaba.fastjson.JSONObject;
import com.pueeo.common.constant.SysConstant;
import com.pueeo.common.support.LoginUser;
import com.pueeo.common.support.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Slf4j
@Activate(group = {CommonConstants.CONSUMER})
public class UserContextConsumerFilter implements Filter {
 
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        LoginUser loginUser = UserContext.get();
        String userInfo;
        if (loginUser != null) {
            userInfo = JSONObject.toJSONString(loginUser);
        } else {
            userInfo = invocation.getAttachment(SysConstant.USER_CONTEXT_RPC_ATTACHMENT);
        }
        invocation.setAttachment(SysConstant.USER_CONTEXT_RPC_ATTACHMENT, userInfo);
        return invoker.invoke(invocation);
    }
}