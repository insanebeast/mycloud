package com.pueeo.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class ClassScanner implements ResourceLoaderAware {

    /**
     * 保存过滤规则要排除的注解
     */
    private final List<TypeFilter> includeFilters = new LinkedList<>();
    private final List<TypeFilter> excludeFilters = new LinkedList<>();
 
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
 
    public static <T> Set<Class<T>> scan(String[] basePackages, Class<? extends Annotation>... annotations) {
        ClassScanner cs = new ClassScanner();

        if(ArrayUtils.isNotEmpty(annotations)) {
            for (Class<? extends Annotation> anno : annotations) {
                cs.addIncludeFilter(new AnnotationTypeFilter(anno));
            }
        }

        Set<Class<T>> classes = new HashSet<>();
        for (String s : basePackages){
            classes.addAll(cs.doScan(s));
        }
        return classes;
    }
 
    public static <T> Set<Class<T>> scan(String basePackages, Class<? extends Annotation>... annotations) {
        return scan(StringUtils.tokenizeToStringArray(basePackages, ",; \t\n"), annotations);
    }

    public static <T> Class<T> scan(String className){
        String basePackage = org.apache.commons.lang3.StringUtils.substringBeforeLast(className, ".");
        Map<String, Class<T>> map = scan(basePackage, null)
                .stream().collect(Collectors.toMap(Class::getName, a -> (Class<T>) a));
        return map.get(className);
    }


    public final ResourceLoader getResourceLoader() {
        return this.resourcePatternResolver;
    }
 
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }
 
    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }
 
    public void addExcludeFilter(TypeFilter excludeFilter) {
        this.excludeFilters.add(0, excludeFilter);
    }
 
    public void resetFilters(boolean useDefaultFilters) {
        this.includeFilters.clear();
        this.excludeFilters.clear();
    }

    private <T> Set<Class<T>> doScan(String basePackage) {
        Set<Class<T>> classes = new HashSet<>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + org.springframework.util.ClassUtils
                    .convertClassNameToResourcePath(SystemPropertyUtils
                            .resolvePlaceholders(basePackage))
                    + "/**/*.class";
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);

            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    boolean match = (includeFilters.size() == 0 && excludeFilters.size() == 0) || matches(metadataReader);
                    if (match) {
                        try {
                            classes.add((Class<T>) Class.forName(metadataReader.getClassMetadata().getClassName()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                    "I/O failure during classpath scanning", ex);
        }
        return classes;
    }
 
    protected boolean matches(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Class scan = ClassScanner.scan("com.pueeo.post.config.PostSysConfig");
        System.out.println(scan);
    }
}