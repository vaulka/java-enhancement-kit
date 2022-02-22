package com.pongsky.kit.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * 反射工具类
 *
 * @author pengsenhao
 */
@Slf4j
public class ReflectUtils {

    /**
     * 获取属性值
     *
     * @param obj   obj
     * @param field 字段
     * @return 获取属性值
     * @author pengsenhao
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
     * @author pengsenhao
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
