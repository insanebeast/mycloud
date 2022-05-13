package com.pueeo.post.service;

import com.pueeo.common.support.LoginUser;
import com.pueeo.post.entity.dto.PostDTO;
import com.pueeo.post.entity.vo.PostVO;

public interface PostService {

    void add(PostDTO postDTO);

    PostVO get(Long id);

    LoginUser info();
}
