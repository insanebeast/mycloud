package com.pueeo.oauth.config;

import com.pueeo.common.constant.TokenConstant;
import com.pueeo.oauth.support.AuthUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.LinkedHashMap;

/**
 * JWT令牌配置类
 */
@Configuration
public class AccessTokenConfig {
    /**
     * 令牌的存储策略
     */
    @Bean
    public TokenStore tokenStore() {
        //使用JwtTokenStore生成JWT令牌
        return new JwtTokenStore(customAccessTokenEnhancer());
    }

    /**
     * 将自定义令牌增强交给Spring管理
     * TokenEnhancer的子类，在JWT编码的令牌值和OAuth身份验证信息之间进行转换。
     * TODO：后期可以使用非对称加密
     */
    @Bean
    public CustomAccessTokenEnhancer customAccessTokenEnhancer(){
        CustomAccessTokenEnhancer enhancer = new CustomAccessTokenEnhancer();
        // 设置秘钥
        enhancer.setSigningKey(TokenConstant.SIGN_KEY);
        /*
         * 设置自定义得的令牌转换器，从map中转换身份信息
         * fix(*)：修复刷新令牌无法获取用户详细信息的问题
         */
        enhancer.setAccessTokenConverter(new CustomAccessTokenConverter());
        return enhancer;
    }

    /**
     * 自定义令牌增强，继承JwtAccessTokenConverter
     * 将业务所需的额外信息放入令牌中，这样下游微服务就能解析令牌获取
     */
    public static class CustomAccessTokenEnhancer extends JwtAccessTokenConverter {
        /**
         * 重写enhance方法，在其中扩展
         */
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            Object principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof AuthUser){
                //获取userDetailService中查询到用户信息
                AuthUser user = (AuthUser)principal;
                //将额外的信息放入到LinkedHashMap中
                LinkedHashMap<String,Object> extendInformation=new LinkedHashMap<>();
                extendInformation.put(TokenConstant.USER_ID, user.getUid());
                //添加到additionalInformation
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extendInformation);
            }
            return super.enhance(accessToken, authentication);
        }
    }
}