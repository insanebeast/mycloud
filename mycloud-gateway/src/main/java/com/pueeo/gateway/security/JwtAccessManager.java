package com.pueeo.gateway.security;

import com.pueeo.common.support.redis.RedisService;
import com.pueeo.common.constant.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 鉴权管理器
 * 用于认证成功之后对用户的权限进行鉴权
 */
@Slf4j
@Component
//@Primary
public class JwtAccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisService redisService;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String path = authorizationContext.getExchange().getRequest().getURI().getPath();
        //redis中获取所有的uri->角色对应关系
        Map<Object, Object> entries = redisService.hkeys(SysConstant.OAUTH_URLS);
        //角色集合
        List<String> authorities = new ArrayList<>();
        entries.forEach((url, roles) -> {
            //路径匹配则添加到角色集合中
            if (antPathMatcher.match(url.toString(), path)) {
                authorities.addAll((List<String>)roles);
            }
        });
        //认证通过且角色匹配的用户可访问当前路径
         return mono
                //判断是否认证成功
                .filter(Authentication::isAuthenticated)
                //获取认证后的全部权限
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                //如果权限包含则判断为true
                .any(authority->{
                    //超级管理员直接放行
                    if (Objects.equals(SysConstant.ROLE_ROOT_CODE, authority))
                        return true;
                    //其他必须要判断角色是否存在交集
                    return !CollectionUtils.isEmpty(authorities) && authorities.contains(authority);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
