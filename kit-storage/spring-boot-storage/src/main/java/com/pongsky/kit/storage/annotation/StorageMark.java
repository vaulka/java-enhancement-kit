package com.pongsky.kit.storage.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pongsky.kit.storage.serializer.StorageMarkJsonSerializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 云存储资源标记
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
@JsonSerialize(using = StorageMarkJsonSerializer.class)
public @interface StorageMark {


}
