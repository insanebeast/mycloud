package com.pueeo.common.support.orika;

import com.pueeo.common.utils.BeanMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrikaBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BeanMapperRegistry) {
            ((BeanMapperRegistry) bean).registry(BeanMapper.getMapperFactory());
        }
        return bean;
    }
}