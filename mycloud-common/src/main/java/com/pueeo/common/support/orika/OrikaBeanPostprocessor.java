package com.pueeo.common.support.orika;

import com.pueeo.common.utils.BeanUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrikaBeanPostprocessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BeanMapperRegistry) {
            ((BeanMapperRegistry) bean).registry(BeanUtil.getMapperFactory());
        }
        return bean;
    }
}