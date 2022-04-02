package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Boolean 处理器
 *
 * @author pengsenhao
 **/
public class ExcelBooleanImportHandler implements ExcelImportHandler {

    private static final Map<String, Boolean> BOOLEAN_MAP = new HashMap<String, Boolean>(16) {
        {
            put("是", Boolean.TRUE);
            put("否", Boolean.FALSE);
        }
    };

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        Boolean value = BOOLEAN_MAP.get(str);
        if (value == null) {
            return;
        }
        this.setValue(result, field, value);
    }

}
