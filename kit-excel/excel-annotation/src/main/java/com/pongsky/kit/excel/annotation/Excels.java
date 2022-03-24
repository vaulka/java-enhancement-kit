package com.pongsky.kit.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 信息 列表
 *
 * @author pengsenhao
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excels {

    /**
     * excel 信息 列表
     *
     * @return excel 信息 列表
     */
    Excel[] value();

}
