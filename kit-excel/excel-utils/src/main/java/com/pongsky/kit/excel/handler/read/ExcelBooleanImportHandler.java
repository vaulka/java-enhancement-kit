package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Boolean 处理器
 *
 * @author pengsenhao
 **/
public class ExcelBooleanImportHandler implements ExcelImportHandler {

    private static final Map<String, Boolean> MAP = new ConcurrentHashMap<String, Boolean>(4) {
        private static final long serialVersionUID = 7779455080979782948L;

        {
            put("是", Boolean.TRUE);
            put("否", Boolean.FALSE);
        }
    };

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        if (obj instanceof Boolean) {
            // 如果是布尔类型，则直接处理
            ReflectUtils.setValue(result, field, obj);
            return;
        }
        // 如果不是布尔类型，则默认按照字符串类型处理
        String str = obj != null
                ? obj.toString()
                : excelProperty.contentStyle().defaultValue();
        Boolean value = MAP.get(str);
        if (value == null) {
            return;
        }
        ReflectUtils.setValue(result, field, value);
    }

}
