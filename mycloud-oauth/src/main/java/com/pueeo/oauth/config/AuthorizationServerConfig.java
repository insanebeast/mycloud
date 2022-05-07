package com.pueeo.oauth.config;

import com.pueeo.common.support.ResultEnum;
import com.pueeo.common.utils.ResponseUtil;
import com.pueeo.oauth.exception.CustomClientCredentialsTokenEndpointFilter;
import com.pueeo.oauth.exception.CustomWebResponseExceptionTranslator;
import com.pueeo.oauth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * OAuth2.0授权服务配置类
 * @EnableAuthorizationServer：这个注解标注这是一个认证中心
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 令牌存储策略
     */
    @Autowired
    private TokenStore tokenStore;
    /**
     * 客户端存储策略，这里使用内存方式，后续可以存储在数据库
     */
    @Autowired
    private ClientDetailsService clientDetailsService;
    /**
     * Security的认证管理器，密码模式需要用到
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * 令牌增强
     */
    @Autowired
    private AccessTokenConfig.CustomAccessTokenEnhancer tokenEnhancer;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * 配置客户端详情，并不是所有的客户端都能接入授权服务
     * 即配置ClientDetailsService
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //TODO 暂定内存模式，后续可以存储在数据库中，更加方便
        clients.inMemory()
                //客户端id client_id
                .withClient("mycloud")
                //客户端秘钥 secret
                .secret(new BCryptPasswordEncoder().encode("123"))
                //资源id，唯一，比如订单服务作为一个资源,可以设置多个
                .resourceIds("user","post")
                //授权模式，总共四种：authorization_code（授权码模式）、password（密码模式）、client_credentials（客户端模式）、implicit（简化模式）
                //refresh_token并不是授权模式
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
                //允许的授权范围 scope，客户端的权限，这里的all只是一种标识，可以自定义，为了后续的资源服务进行权限控制
                .scopes("all")
                //false 则跳转到授权页面
                .autoApprove(false)
                //授权码模式的回调地址
                .redirectUris("http://www.baidu.com");
    }

    /**
     * 令牌管理服务的配置
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        //客户端端配置策略
        services.setClientDetailsService(clientDetailsService);
        //支持令牌的刷新
        services.setSupportRefreshToken(true);
        //令牌服务
        services.setTokenStore(tokenStore);
        //access_token的过期时间
        services.setAccessTokenValiditySeconds(60 * 60 * 2); //2hour
        //refresh_token的过期时间
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3); //3day
        //设置令牌增强
        services.setTokenEnhancer(tokenEnhancer);
        return services;
    }

    /**
     * 授权码模式的service，使用授权码模式authorization_code必须注入
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        //todo 授权码暂时存在内存中，后续可以存储在数据库中
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 配置令牌访问的端点
     */
    @Override
    @SuppressWarnings("ALL")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                //如果需要使用refresh_token模式则需要注入userDetailService
                .userDetailsService(customUserDetailsService)
                //设置异常WebResponseExceptionTranslator，用于处理用户名，密码错误、授权类型不正确的异常
                .exceptionTranslator(new CustomWebResponseExceptionTranslator())
                //授权码模式所需要的authorizationCodeServices
                .authorizationCodeServices(authorizationCodeServices())
                //密码模式所需要的authenticationManager
                .authenticationManager(authenticationManager)
                //令牌管理服务，无论哪种模式都需要
                .tokenServices(tokenServices())
                //只允许POST提交访问令牌，uri：/oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 配置令牌访问的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer configurer) {
        //自定义ClientCredentialsTokenEndpointFilter，用于处理client_id, client_secret错误的异常
        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(configurer);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint());
        configurer.addTokenEndpointAuthenticationFilter(endpointFilter);
        configurer
                .authenticationEntryPoint(authenticationEntryPoint())
                //开启/oauth/token_key验证端口权限访问
                .tokenKeyAccess("permitAll()")
                //开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("permitAll()");
                //一定不要添加allowFormAuthenticationForClients，否则自定义的CustomClientCredentialsTokenEndpointFilter不生效
                //.allowFormAuthenticationForClients();
    }

    /**
     * 自定义客户端异常处理
     * 用于处理客户端认证出错，包括client_id, client_secret错误
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseUtil.writeJson(response, ResultEnum.CLIENT_AUTHENTICATION_FAILED);
        };
    }
}