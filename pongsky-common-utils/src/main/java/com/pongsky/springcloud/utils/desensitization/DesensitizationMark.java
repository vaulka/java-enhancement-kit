package com.pongsky.springcloud.utils.desensitization;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据脱敏标记
 * <p>
 * 使用规则如下：
 * 1、将该注解注解于 field（字段上）/ method（方法上）
 * <p>
 * 2、field type（字段类型）/ method returnType（方法返回值类型）必须为 String / Class
 *
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-08 3:34 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DesensitizationMark {

    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-08 3:36 下午
     */
    DesensitizationType type();

}
