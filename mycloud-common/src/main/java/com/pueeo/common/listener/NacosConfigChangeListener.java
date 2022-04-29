package com.pueeo.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * 监听nacos配置发生变更
 */
@Component
public class NacosConfigChangeListener implements ApplicationListener<EnvironmentChangeEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConfigurableEnvironment environment;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        for (String key : event.getKeys()) {
            logger.info("【onApplicationEvent】key({}) 最新 value 为 {}", key, environment.getProperty(key));
        }
    }

}