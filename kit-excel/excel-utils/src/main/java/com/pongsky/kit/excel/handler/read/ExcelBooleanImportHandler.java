package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Boolean 处理器
 *
 * @author pengsenhao
 **/
public class ExcelBooleanImportHandler implements ExcelImportHandler {

    private static final Map<String, Boolean> MAP = Map.of(
            "是", Boolean.TRUE,
            "否", Boolean.FALSE
    );

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        if (obj instanceof Boolean) {
            // 如果是布尔类型，则直接处理
            this.setValue(result, field, obj);
            return;
        }
        // 如果不是布尔类型，则默认按照字符串类型处理
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        Boolean value = MAP.get(str);
        if (value == null) {
            return;
        }
        this.setValue(result, field, value);
    }

}
