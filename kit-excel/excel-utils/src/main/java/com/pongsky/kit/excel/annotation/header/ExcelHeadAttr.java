package com.pongsky.kit.excel.annotation.header;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 标题属性
 *
 * @author pengsenhao
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHeadAttr {

    /**
     * 标题下标
     *
     * @return 标题下标
     */
    int headIndex();

    /**
     * 动态值下标
     *
     * @return 动态值下标
     */
    int valueIndex();

}