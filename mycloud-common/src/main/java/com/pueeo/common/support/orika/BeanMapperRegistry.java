package com.pueeo.common.support.orika;

import ma.glasnost.orika.MapperFactory;

/**
 * 定义映射规则
 * 后续新的映射规则实现BeanMapperRegistry即可
 */
public interface BeanMapperRegistry {
    void registry(MapperFactory mapperFactory);
}
