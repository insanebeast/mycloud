package com.pueeo.post.service.impl;

import com.google.common.collect.Maps;
import com.pueeo.common.exception.ServiceException;
import com.pueeo.common.support.ResultEnum;
import com.pueeo.common.utils.SnowflakeIdUtil;
import com.pueeo.post.entity.Post;
import com.pueeo.post.entity.dto.PostDTO;
import com.pueeo.post.entity.vo.PostVO;
import com.pueeo.post.service.PostService;
import com.pueeo.user.entity.dto.UserDTO;
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
        UserDTO userDTO = userService.get(postDTO.getUid());
        if (userDTO == null){
            throw new ServiceException(ResultEnum.USER_NOT_EXIST);
        }
        Post post = new Post().setId(SnowflakeIdUtil.getSnowflakeId())
                .setContent(postDTO.getContent())
                .setUid(postDTO.getUid())
                .setCreateTime(new Date());
        postMap.put(post.getId(), post);
    }

    public PostVO get(Long id) {
        Post post = postMap.get(id);
        if (post == null){
            throw new ServiceException(ResultEnum.POST_NOT_EXIST);
        }
        UserDTO userDTO = userService.get(post.getUid());
        return new PostVO().setId(post.getId())
                .setAuthor(userDTO)
                .setCreateTime(post.getCreateTime())
                .setContent(post.getContent());
    }

    public String info() {
        return "info";
    }
}
