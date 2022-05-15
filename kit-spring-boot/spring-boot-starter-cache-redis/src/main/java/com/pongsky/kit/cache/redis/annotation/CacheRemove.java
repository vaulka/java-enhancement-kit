package com.pongsky.kit.cache.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模糊删除 key
 *
 * @author pengsenhao
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CacheRemove {

    /**
     * 用法同 {@link org.springframework.cache.annotation.Cacheable#cacheNames()}
     *
     * @return cacheNames
     */
    String cacheNames();

    /**
     * 用法同 {@link org.springframework.cache.annotation.Cacheable#key()}
     *
     * @return key
     */
    String key() default "";

}