package com.pueeo.common.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.pueeo.common.constant.SysConstant;
import com.pueeo.common.constant.TokenConstant;
import com.pueeo.common.support.LoginUser;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 解密网关传递的用户信息
 * 将其放入request中，便于后续业务方法直接获取用户信息
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的加密的用户信息
        String token = request.getHeader(TokenConstant.TOKEN_NAME);
        if (StringUtils.isNotBlank(token)){
            //解密
            String json = new String(Base64Utils.decodeFromString(token));
            JSONObject jsonObject = JSON.parseObject(json);
            //获取用户身份信息、权限信息
            String principal = jsonObject.getString(TokenConstant.PRINCIPAL_NAME);
            Long userId = jsonObject.getLong(TokenConstant.USER_ID);
            String jti = jsonObject.getString(TokenConstant.JTI);
            Long expireIn = jsonObject.getLong(TokenConstant.EXPR);
            JSONArray tempJsonArray = jsonObject.getJSONArray(TokenConstant.AUTHORITIES_NAME);
            //权限
            List<String> authorities = tempJsonArray.toJavaList(String.class);
            //放入LoginVal
            LoginUser loginUser = new LoginUser()
                    .setUid(userId)
                    .setUsername(principal)
                    .setJti(jti).setExpiresIn(expireIn)
                    .setAuthorities(authorities);
            //放入request的attribute中
            request.setAttribute(SysConstant.USER_CONTEXT_REQUEST_ATTRIBUTE, loginUser);
        }
        filterChain.doFilter(request,response);
    }
}