package com.pueeo.oauth.service;

import com.pueeo.common.utils.SnowflakeIdUtil;
import com.pueeo.oauth.support.AuthUser;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    public static List<AuthUser> users = new ArrayList<>();

    static {
        AuthUser admin = AuthUser.builder()
                .userId(SnowflakeIdUtil.getSnowflakeId())
                .username("admin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .authorities(AuthorityUtils.createAuthorityList("ROLE_user", "ROLE_admin"))
                .build();

        AuthUser user = AuthUser.builder()
                .userId(SnowflakeIdUtil.getSnowflakeId())
                .username("user")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .authorities(AuthorityUtils.createAuthorityList("ROLE_user"))
                .build();
        users.add(admin);
        users.add(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库中查询
        List<AuthUser> list = users.stream()
                .filter(p -> username.equals(p.getUsername())).limit(1)
                .collect(Collectors.toList());
        //用户不存在直接抛出UsernameNotFoundException，security会捕获抛出BadCredentialsException
        if (CollectionUtils.isEmpty(list))
            throw new UsernameNotFoundException("用户不存在");
        return list.get(0);
    }
}