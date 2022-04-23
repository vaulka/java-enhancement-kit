package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Integer Week 处理器
 *
 * @author pengsenhao
 **/
public class ExcelIntegerWeekImportHandler implements ExcelImportHandler {

    private static final Map<String, Integer> MAP = new ConcurrentHashMap<String, Integer>(10) {
        private static final long serialVersionUID = 4828824878326543059L;

        {
            put("星期一", 1);
            put("星期二", 2);
            put("星期三", 3);
            put("星期四", 4);
            put("星期五", 5);
            put("星期六", 6);
            put("星期日", 7);
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
