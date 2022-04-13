package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Integer Month 处理器
 *
 * @author pengsenhao
 **/
public class ExcelIntegerMonthImportHandler implements ExcelImportHandler {

    private static final Map<String, Integer> MAP = new ConcurrentHashMap<String, Integer>(16) {
        {
            put("一月", 1);
            put("二月", 2);
            put("三月", 3);
            put("四月", 4);
            put("五月", 5);
            put("六月", 6);
            put("七月", 7);
            put("八月", 8);
            put("九月", 9);
            put("十月", 10);
            put("十一月", 11);
            put("十二月", 12);
        }
    };

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.contentStyle().defaultValue();
        Integer value = MAP.get(str);
        if (value == null) {
            return;
        }
        ReflectUtils.setValue(result, field, value);
    }

}
