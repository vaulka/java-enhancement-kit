package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Integer Week 处理器
 *
 * @author pengsenhao
 **/
public class ExcelIntegerWeekImportHandler implements ExcelImportHandler {

    private static final Map<String, Integer> MAP = Map.of(
            "星期一", 1,
            "星期二", 2,
            "星期三", 3,
            "星期四", 4,
            "星期五", 5,
            "星期六", 6,
            "星期日", 7
    );

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        Integer value = MAP.get(str);
        if (value == null) {
            return;
        }
        this.setValue(result, field, value);
    }

}
