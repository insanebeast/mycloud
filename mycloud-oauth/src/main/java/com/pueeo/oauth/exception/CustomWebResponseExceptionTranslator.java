package com.pueeo.oauth.exception;

import com.pueeo.common.support.ApiResult;
import com.pueeo.common.support.ResultEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * 自定义异常翻译器
 * 针对OAuth2Exception异常，即「授权模式异常」和「账号密码类的异常」（grant_type、username、password）
 * 「客户端异常」（client_id、client_secret）需要经过ClientCredentialsTokenEndpointFilter
 * 交由OAuth2AuthenticationEntryPoint这个认证入口处理
 * 因此需要重写ClientCredentialsTokenEndpointFilter
 * @see com.pueeo.oauth.exception.CustomClientCredentialsTokenEndpointFilter
 */
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator{
    /**
     * 业务处理方法，重写这个方法返回客户端信息
     */
    @Override
    public ResponseEntity<ApiResult> translate(Exception e){
        ApiResult resultMsg = doTranslateHandler(e);
        return new ResponseEntity<>(resultMsg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 根据异常定制返回信息
     * TODO 自己根据业务封装
     */
    private ApiResult doTranslateHandler(Exception e) {
        ResultEnum resultEnum = ResultEnum.SYSTEM_ERROR;
        if(e instanceof UnsupportedGrantTypeException){
            resultEnum = ResultEnum.UNSUPPORTED_GRANT_TYPE;
        }else if(e instanceof InvalidGrantException){ //账号密码错误
            resultEnum = ResultEnum.USERNAME_OR_PASSWORD_ERROR;
        } else if (e instanceof InvalidScopeException){
            resultEnum = ResultEnum.INVALID_SCOPE;
        }else if (e instanceof InvalidRequestException){
            resultEnum = ResultEnum.INVALID_REQUEST;
        }else if (e instanceof UsernameNotFoundException){
            resultEnum = ResultEnum.USER_ACCOUNT_NOT_EXIST;
        }else if (e instanceof BadCredentialsException){
            resultEnum = ((BadCredentialsException) e).getResultEnum();
        }else if (e instanceof InternalAuthenticationServiceException) {
            return ApiResult.fail(e.getMessage());
        }
        return ApiResult.fail(resultEnum);
    }
}
