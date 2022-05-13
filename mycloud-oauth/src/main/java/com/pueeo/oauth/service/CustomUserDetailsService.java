package com.pueeo.oauth.service;

import com.pueeo.common.support.ResultEnum;
import com.pueeo.oauth.exception.BadCredentialsException;
import com.pueeo.oauth.support.AuthUser;
import com.pueeo.user.constant.UserConst;
import com.pueeo.user.entity.UserAccount;
import com.pueeo.user.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements AbstractUserDetailsService {

    @Autowired
    private UserAccountService userAccountService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountService.lambdaQuery()
                .eq(UserAccount::getUsername, username)
                .eq(UserAccount::getStatus, UserConst.AccountStatus.normal)
                .oneOpt().orElseThrow(() -> new BadCredentialsException(ResultEnum.USERNAME_OR_PASSWORD_ERROR));

        return AuthUser.builder().uid(userAccount.getUid())
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .authorities(AuthorityUtils.createAuthorityList("ROLE_appuser"))
                .build();
    }

    @Override
    public UserDetails loadByPhone(String phone) {
        UserAccount userAccount = userAccountService.lambdaQuery()
                .eq(UserAccount::getPhone, phone)
                .eq(UserAccount::getStatus, UserConst.AccountStatus.normal)
                .oneOpt().orElseThrow(() -> new BadCredentialsException(ResultEnum.PHONE_OR_CODE_ERROR));

        return AuthUser.builder().uid(userAccount.getUid())
                .username(userAccount.getUsername())
                .authorities(AuthorityUtils.createAuthorityList("ROLE_appuser"))
                .build();
    }
}