package com.pongsky.kit.utils.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
     * 默认频率
     */
    int DEFAULT_FREQUENCY = 100;

    /**
     * 防重频率，单位毫秒
     *
     * @return 防重频率，单位毫秒
     */
    int frequency() default DEFAULT_FREQUENCY;

}
