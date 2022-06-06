package com.pongsky.kit.desensitization.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pongsky.kit.desensitization.handler.DesensitizationHandler;
import com.pongsky.kit.desensitization.serializer.DesensitizationMarkJsonSerializer;

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
 */
@Documented
@JacksonAnnotationsInside
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@JsonSerialize(using = DesensitizationMarkJsonSerializer.class)
public @interface DesensitizationMark {

    /**
     * 脱敏处理器
     *
     * @return 脱敏处理器
     */
    Class<? extends DesensitizationHandler> handler();

}
