package com.pueeo.dubbo.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.pueeo.common.constant.SysConstant;
import com.pueeo.common.support.LoginUser;
import com.pueeo.common.support.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Slf4j
@Activate(group = {CommonConstants.PROVIDER})
public class UserContextProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String userInfo = invocation.getAttachment(SysConstant.USER_CONTEXT_RPC_ATTACHMENT);
        if (StringUtils.isBlank(userInfo)) {
            return invoker.invoke(invocation);
        }
        try {
            LoginUser loginUser = JSONObject.parseObject(userInfo, LoginUser.class);
            UserContext.set(loginUser);
            return invoker.invoke(invocation);
        } finally {
            UserContext.clear();
        }
    }
}