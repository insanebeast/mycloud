package com.pueeo.gateway.config;

import com.pueeo.gateway.exception.access.CustomServerAccessDeniedHandler;
import com.pueeo.gateway.exception.access.CustomServerAuthenticationEntryPoint;
import com.pueeo.gateway.filter.CorsFilter;
import com.pueeo.gateway.security.JwtAccessManager;
import com.pueeo.gateway.security.JwtAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * 网关Spring Security安全配置类
 * 由于spring cloud gateway使用的Flux，因此需要使用@EnableWebFluxSecurity注解开启，而不是平常的web应用了
 */
@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    /**
     * 认证管理器
     */
    @Autowired
    private JwtAuthenticationManager jwtAuthenticationManager;
    /**
     * 鉴权管理器
     */
    @Autowired
    private JwtAccessManager jwtAccessManager;
    /**
     * 没有登录或token过期的异常处理
     */
    @Autowired
    private CustomServerAuthenticationEntryPoint customServerAuthenticationEntryPoint;
    /**
     * 权限不足的异常处理
     */
    @Autowired
    private CustomServerAccessDeniedHandler customServerAccessDeniedHandler;
    /**
     * 系统参数配置
     */
    @Autowired
    private OAuthConfig oAuthConfig;
    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception{
        //认证过滤器，放入认证管理器
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());

        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                //白名单直接放行
                .pathMatchers(oAuthConfig.getIgnoreUrls().toArray(new String[]{})).permitAll()
                //其他的请求必须鉴权，使用鉴权管理器
                .anyExchange().access(jwtAccessManager)
                //鉴权的异常处理，权限不足，token失效
                .and().exceptionHandling()
                .authenticationEntryPoint(customServerAuthenticationEntryPoint)
                .accessDeniedHandler(customServerAccessDeniedHandler)
                .and()
                // 跨域过滤器
                .addFilterAt(corsFilter, SecurityWebFiltersOrder.CORS)
                //token的认证过滤器，用于校验token和认证
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}