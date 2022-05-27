package com.pueeo.common.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;


import java.util.List;

public class BeanMapper {
    private static final MapperFactory mapperFactory;
    private static final MapperFacade mapperFacade;

    static {
        mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    public static MapperFactory getMapperFactory() {
        return mapperFactory;
    }

    /**
     * 将源对象的值拷贝到目标对象中，源对象中的null属性将被忽略
     */
    public static <S, D> void copy(S source, D target) {
        mapperFacade.map(source, target);
    }

    /**
     * 简单的复制出新类型对象
     * 通过source.getClass() 获得源Class
     */
    public static <S, T> T map(S source, Class<T> targetClass) {
        return mapperFacade.map(source, targetClass);
    }

    /**
     * 极致性能的复制出新类型对象
     * 预先通过BeanUtil.getType() 静态获取并缓存Type类型，在此处传入
     */
    public static <S, D> D map(S source, Type<S> sourceType, Type<D> destinationType) {
        return mapperFacade.map(source, sourceType, destinationType);
    }

    /**
     * 简单的复制出新对象列表到ArrayList
     * 不建议使用mapper.mapAsList(Iterable<S>,Class<D>)接口, sourceClass需要反射，效率低
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destinationClass) {
        return mapperFacade.mapAsList(sourceList, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
    }

    /**
     * 极致性能的复制出新类型对象到ArrayList
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destinationType) {
        return mapperFacade.mapAsList(sourceList, sourceType, destinationType);
    }

    /**
     * 简单复制出新对象列表到数组
     * 通过source.getComponentType() 获得源Class
     */
    public static <S, D> D[] mapArray(final D[] destination, final S[] source, final Class<D> destinationClass) {
        return mapperFacade.mapAsArray(destination, source, destinationClass);
    }

    /**
     * 极致性能的复制出新类型对象到数组
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     */
    public static <S, D> D[] mapArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return mapperFacade.mapAsArray(destination, source, sourceType, destinationType);
    }

    /**
     * 预先获取orika转换所需要的Type，避免每次转换
     */
    public static <E> Type<E> getType(final Class<E> rawType) {
        return TypeFactory.valueOf(rawType);
    }
}