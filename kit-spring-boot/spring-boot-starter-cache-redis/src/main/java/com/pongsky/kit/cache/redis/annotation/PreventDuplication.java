package com.pongsky.kit.cache.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 防重检测标记
 *
 * @author pengsenhao
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface PreventDuplication {

    /**
     * 默认访问频率
     */
    int DEFAULT_FREQUENCY = 100;

    /**
     * 访问频率
     *
     * @return 访问频率
     */
    int frequency() default DEFAULT_FREQUENCY;

    /**
     * 默认时间单位
     */
    TimeUnit DEFAULT_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

}