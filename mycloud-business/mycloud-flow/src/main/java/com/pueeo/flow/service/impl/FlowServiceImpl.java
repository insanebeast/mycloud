package com.pueeo.flow.service.impl;

import com.pueeo.flow.service.FlowService;
import com.pueeo.user.entity.dto.UserDTO;
import com.pueeo.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@DubboService(protocol = "dubbo")
@Service
public class FlowServiceImpl implements FlowService {

    @DubboReference(protocol = "dubbo")
    private UserService userService;

    public String add(Long uid, String content) {
        UserDTO userDTO = userService.get(uid);
        return userDTO.getName() + "发布了帖子，内容为：" + content;
    }
}
