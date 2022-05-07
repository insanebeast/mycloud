package com.pueeo.oauth.config;

import com.pueeo.common.constant.SysConstant;
import com.pueeo.common.constant.TokenConstant;
import com.pueeo.oauth.support.AuthUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 自定义令牌转换器
 * 将身份认证信息转换后存储在令牌内
 */
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {
    public CustomAccessTokenConverter(){
        super.setUserTokenConverter(new CustomUserAuthenticationConverter());
    }

    /**
     * 自定义用户身份认证转换器
     */
    public static class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

        /**
         * 重写抽取用户数据方法
         * 从map中提提取用户信息
         */
        @Override
        public Authentication extractAuthentication(Map<String, ?> map) {
            if (map.containsKey(USERNAME)) {
                Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
                String username = (String) map.get(USERNAME);
                Long userId = Long.parseLong(map.get(TokenConstant.USER_ID).toString());
                AuthUser user = new AuthUser(userId, username, "", authorities);
                return new UsernamePasswordAuthenticationToken(user, "", authorities);
            }
            return null;
        }

        /**
         * 提取权限
         */
        private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
            Object authorities = map.get(AUTHORITIES);
            if (authorities instanceof String) {
                return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
            }
            if (authorities instanceof Collection) {
                return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                        .collectionToCommaDelimitedString((Collection<?>) authorities));
            }
            throw new IllegalArgumentException("Authorities must be either a String or a Collection");
        }

    }
}


