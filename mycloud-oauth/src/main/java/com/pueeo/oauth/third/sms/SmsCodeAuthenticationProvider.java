package com.pueeo.oauth.third.sms;

import com.alibaba.fastjson.JSONObject;
import com.pueeo.common.support.RedisKey;
import com.pueeo.common.support.ResultEnum;
import com.pueeo.common.support.redis.RedisService;
import com.pueeo.oauth.exception.BadCredentialsException;
import com.pueeo.oauth.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * 自定义AuthenticationProvider:短信验证码登陆
 */
@Slf4j
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailService;
    private RedisService redisService;

    public SmsCodeAuthenticationProvider(CustomUserDetailsService userDetailService, RedisService redisService){
        this.userDetailService = userDetailService;
        this.redisService = redisService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String phone = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();
        //查询数据库，加载用户详细信息
        UserDetails user = userDetailService.loadByPhone(phone);
        log.info("SmsCodeAuthenticationProvider load user: {}", JSONObject.toJSONString(user));

        String codeInCache = (String)redisService.get(RedisKey.SMS_CODE_LOGIN.getKey(phone));
        if (!Objects.equals(codeInCache, code)) {
            throw new BadCredentialsException(ResultEnum.PHONE_OR_CODE_ERROR);
        }
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, code, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    /**
     * Spring Security会根据这个方法判断当前Provider是否支持处理
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}