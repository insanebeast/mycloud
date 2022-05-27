package com.pueeo.common.support.sysconf;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.pueeo.common.config.SysNacosConfig;
import com.pueeo.common.utils.ClassScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author pueeo
 * @date 2022-05-24
 */
@Component
@Slf4j
public class SysConfigService {

    @Resource
    private SysNacosConfig sysNacosConfig;
    @Resource
    private ObjectMapper objectMapper;

    private static final String SYS_CONF_DATA_ID = "system-config.yaml";
    private ConfigService configService;

    @Autowired
    public SysConfigService(NacosDiscoveryProperties nacosDiscoveryProperties) {
        try {
            configService = NacosFactory.createConfigService(nacosDiscoveryProperties.getNacosProperties());
        } catch (NacosException e) {
            log.error("nacos configService init error ",e);
        }
    }

    /**
     * 获取配置
     *
     * @param configEnum
     * @return
     * @param <T>
     */
    public <T> T getConfig(SysConfigEnum configEnum) {
        Object o = sysNacosConfig.getData().get(configEnum.getKey());
        Class<T> clazz = ClassScanner.scan(configEnum.getClazz());
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper.convertValue(o, clazz);
    }

    /**
     * 更新配置
     *
     * @param configEnum
     * @param t
     * @param <T>
     */
    public <T> void updateConfig(SysConfigEnum configEnum, T t) {
        Map<String, Object> map = sysNacosConfig.getData();
        map.put(configEnum.getKey(), objectMapper.convertValue(t, LinkedHashMap.class));
        try {
            publishConfig(map);
        } catch (NacosException e) {
            log.error("nacos updateConfig error ",e);
        }
    }

    private void publishConfig(Map<String, Object> map) throws NacosException {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setPrettyFlow(false);
        Yaml yaml = new Yaml(dumperOptions);
        String content = yaml.dumpAsMap(ImmutableMap.of(SysNacosConfig.PREFIX, ImmutableMap.of("data", map)));
        configService.publishConfig(SYS_CONF_DATA_ID, "DEFAULT_GROUP", content, ConfigType.YAML.getType());
    }
}
