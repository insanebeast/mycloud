package com.pueeo.user.service.impl;

import com.pueeo.user.entity.UserProfile;
import com.pueeo.user.mapper.UserProfileMapper;
import com.pueeo.user.service.UserProfileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户资料表 服务实现类
 * </p>
 *
 * @author pueeo
 * @since 2022-05-09
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {

}
