package com.pongsky.kit.type.parser.utils;

import java.lang.reflect.Field;

/**
 * 数组 工具类
 *
 * @author pengsenhao
 **/
public class ArrayUtils {

    /**
     * 获取数组的元素类型
     *
     * @param array 数组列表
     * @return 获取数组的元素类型
     */
    public static Class<?> getElementType(Object[] array) {
        if (array == null) {
            return null;
        }
        return array.getClass().getComponentType();
    }

    /**
     * 获取数组的元素类型
     *
     * @param field 字段
     * @return 获取数组的元素类型
     */
    public static Class<?> getElementType(Field field) {
        if (field == null) {
            return null;
        }
        return field.getType().getComponentType();
    }

    /**
     * 获取数组的元素类型
     *
     * @param clazz 类
     * @return 获取数组的元素类型
     */
    public static Class<?> getElementType(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return clazz.getComponentType();
    }

}
