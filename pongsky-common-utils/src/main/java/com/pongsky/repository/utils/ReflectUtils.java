package com.pongsky.repository.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-14 2:03 下午
 */
@Slf4j
public class ReflectUtils {

    /**
     * 获取属性值
     *
     * @param obj   obj
     * @param field 字段
     * @return 获取属性值
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-08 7:01 下午
     */
    public static Object getValue(Object obj, Field field) {
        field.setAccessible(true);
        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 设置属性值
     *
     * @param obj    obj
     * @param field  字段
     * @param result 值
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-14 2:05 下午
     */
    public static void setValue(Object obj, Field field, Object result) {
        field.setAccessible(true);
        try {
            field.set(obj, result);
        } catch (IllegalAccessException e) {
            log.error(e.getLocalizedMessage());
        }
    }

}
