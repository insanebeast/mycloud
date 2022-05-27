package com.pueeo.common.config;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = SysNacosConfig.PREFIX)
public class SysNacosConfig {

    public static final String PREFIX = "sysconf";

    private Map<String, Object> data = Maps.newHashMap();

}
