package com.pongsky.kit.type.parser.utils;

import java.util.List;

/**
 * 类ss解析 工具类
 *
 * @author pengsenhao
 **/
public class ClassParserUtils {

    /**
     * 递归获取父 class 列表
     *
     * @param classes class 列表
     * @param clazz   class
     */
    public static void getSuperClasses(List<Class<?>> classes, Class<?> clazz) {
        classes.add(clazz);
        if (!clazz.getSuperclass().getName().equals(Object.class.getName())) {
            ClassParserUtils.getSuperClasses(classes, clazz.getSuperclass());
        }
    }

}
