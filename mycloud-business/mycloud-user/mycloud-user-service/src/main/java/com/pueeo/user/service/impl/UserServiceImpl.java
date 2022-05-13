package com.pueeo.user.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Maps;
import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.support.LoginUser;
import com.pueeo.common.support.ResultEnum;
import com.pueeo.common.support.UserContext;
import com.pueeo.user.entity.dto.UserRegisterDTO;
import com.pueeo.user.entity.vo.UserProfileVO;
import com.pueeo.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Map;

@DubboService(protocol = "dubbo")
@Service
public class UserServiceImpl implements UserService {

    private static final Map<Long, UserProfileVO> userMap;
    static {
        userMap = Maps.newConcurrentMap();
        userMap.put(10000L, new UserProfileVO().setUid(10000L).setGender(1).setNickname("周杰伦"));
        userMap.put(10001L, new UserProfileVO().setUid(10001L).setGender(2).setNickname("蔡依林"));
        userMap.put(10002L, new UserProfileVO().setUid(10002L).setGender(2).setNickname("黄丽玲"));
    }

    @SentinelResource(value = "user.get")
    public UserProfileVO get(Long uid){
        if (uid <= 0){
            throw new BusinessException(ResultEnum.PARAMETER_ILLEGAL);
        }
        return userMap.get(uid);
    }

    @Override
    public LoginUser currentUser() {
        LoginUser loginUser = UserContext.get();
        return loginUser;
    }

//
//    public static ApiResult handleException(BlockException exception) {
//        return ApiResult.fail(300, String.format("应用【%s】触发【流量控制】",exception.getRuleLimitApp()));
//    }

}
