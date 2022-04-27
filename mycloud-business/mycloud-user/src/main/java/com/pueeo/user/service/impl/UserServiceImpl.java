package com.pueeo.user.service.impl;

import com.pueeo.common.exception.RpcServiceException;
import com.pueeo.common.support.ResultEnum;
import com.pueeo.user.entity.dto.UserDTO;
import com.pueeo.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@DubboService(protocol = "dubbo")
@Service
public class UserServiceImpl implements UserService {

    public UserDTO get(Long uid){
        if (uid == 0){
            throw new RpcServiceException(ResultEnum.PARAMETER_ILLEGAL);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUid(uid);
        userDTO.setGender((byte)1);
        userDTO.setName("小王");
        return userDTO;
    }

}
