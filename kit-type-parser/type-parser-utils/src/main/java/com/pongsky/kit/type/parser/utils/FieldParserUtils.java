package com.pongsky.kit.type.parser.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段解析 工具类
 *
 * @author pengsenhao
 **/
public class FieldParserUtils {

    /**
     * 递归获取父 字段 列表
     *
     * @param clazz class
     */
    public static List<Field> getSuperFields(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        ClassParserUtils.getSuperClasses(classes, clazz);
        return classes.stream()
                .map(c -> Arrays.stream(c.getDeclaredFields()).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
