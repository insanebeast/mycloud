package com.pueeo.user.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Maps;
import com.pueeo.common.exception.ServiceException;
import com.pueeo.common.support.ResultEnum;
import com.pueeo.user.entity.dto.UserDTO;
import com.pueeo.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Map;

@DubboService(protocol = "dubbo")
@Service
public class UserServiceImpl implements UserService {

    private static final Map<Long, UserDTO> userMap;
    static {
        userMap = Maps.newConcurrentMap();
        userMap.put(10000L, new UserDTO().setUid(10000L).setGender((byte)1).setName("周杰伦"));
        userMap.put(10001L, new UserDTO().setUid(10001L).setGender((byte)2).setName("蔡依林"));
        userMap.put(10002L, new UserDTO().setUid(10002L).setGender((byte)2).setName("黄丽玲"));
    }

    @SentinelResource(value = "user.get")
    public UserDTO get(Long uid){
        if (uid <= 0){
            throw new ServiceException(ResultEnum.PARAMETER_ILLEGAL);
        }
        return userMap.get(uid);
    }
//
//    public static ApiResult handleException(BlockException exception) {
//        return ApiResult.fail(300, String.format("应用【%s】触发【流量控制】",exception.getRuleLimitApp()));
//    }

}
