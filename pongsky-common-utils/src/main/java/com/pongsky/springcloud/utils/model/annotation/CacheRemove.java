package com.pongsky.springcloud.utils.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模糊删除key
 *
 * @author pengsenhao
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRemove {

    /**
     * @return cacheNames
     * 用法参照 {@link org.springframework.cache.annotation.CacheEvict#cacheNames()} ()}
     */
    String cacheNames() default "";

    /**
     * @return key
     * 用法参照 {@link org.springframework.cache.annotation.CacheEvict#key()}
     */
    String key() default "";

    /**
     * 模糊删除key列表
     *
     * @return 模糊删除key列表
     */
    String[] keys() default {};

}
