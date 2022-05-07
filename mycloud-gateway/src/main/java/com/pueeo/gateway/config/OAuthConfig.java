package com.pueeo.gateway.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OAuth配置
 */
@Configuration
@ConfigurationProperties(prefix = "oauth2.cloud")
@Data
public class OAuthConfig {
    /**
     * 鉴权白名单
     */
    private List<String> ignoreUrls;
}
