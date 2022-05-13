package com.pueeo.post.service.impl;

import com.google.common.collect.Maps;
import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.support.LoginUser;
import com.pueeo.common.support.ResultEnum;
import com.pueeo.common.utils.SnowflakeIdUtil;
import com.pueeo.post.entity.Post;
import com.pueeo.post.entity.dto.PostDTO;
import com.pueeo.post.entity.vo.PostVO;
import com.pueeo.post.service.PostService;
import com.pueeo.user.entity.dto.UserRegisterDTO;
import com.pueeo.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    @DubboReference(protocol = "dubbo")
    private UserService userService;

    private static Map<Long, Post> postMap = Maps.newConcurrentMap();

    public void add(PostDTO postDTO) {

    }

    public PostVO get(Long id) {
        return null;
    }

    public LoginUser info() {
        return userService.currentUser();
    }
}
