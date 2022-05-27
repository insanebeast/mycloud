package com.pueeo.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pueeo.user.entity.UserAccount;
import com.pueeo.user.mapper.UserAccountMapper;
import com.pueeo.user.service.UserAccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账号表 服务实现类
 * </p>
 *
 * @author pueeo
 * @since 2022-05-09
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {


}
