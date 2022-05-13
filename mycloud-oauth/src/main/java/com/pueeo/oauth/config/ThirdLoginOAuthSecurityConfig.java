package com.pueeo.oauth.config;

import com.pueeo.common.support.redis.RedisService;
import com.pueeo.oauth.service.CustomUserDetailsService;
import com.pueeo.oauth.third.sms.SmsCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * 第三方登陆安全配置
 */
@Component
public class ThirdLoginOAuthSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private RedisService redisService;
    @Autowired
    private CustomUserDetailsService userDetailService;

    /**
     * 短信验证码配置器
     *  使用外部配置必须要在WebSecurityConfig中用http.apply(thirdLoginOAuthSecurityConfig)将配置注入进去
     */
    @Override
    public void configure(HttpSecurity builder) {
        //注入SmsCodeAuthenticationProvider
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider(userDetailService, redisService);
        builder.authenticationProvider(smsCodeAuthenticationProvider);
    }
}