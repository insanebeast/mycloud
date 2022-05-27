package com.pueeo.user.service;

import com.pueeo.common.support.LoginUser;
import com.pueeo.user.entity.dto.UserRegisterDTO;
import com.pueeo.user.entity.vo.UserProfileVO;

public interface UserService {

    UserProfileVO get(Long uid);
    UserProfileVO register(UserRegisterDTO userDTO);

    LoginUser currentUser();

}
