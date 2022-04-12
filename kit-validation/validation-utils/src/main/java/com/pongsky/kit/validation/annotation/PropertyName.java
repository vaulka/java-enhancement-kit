package com.pongsky.kit.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验时表示的属性名称
 *
 * @author pengsenhao
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyName {

    /**
     * 属性名称
     * <p>
     * 没有填写则默认获取字段名称
     *
     * @return 属性名称
     */
    String value() default "";

}
