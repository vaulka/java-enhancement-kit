package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;

import java.lang.reflect.Field;
import java.time.Month;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Month 处理器
 *
 * @author pengsenhao
 **/
public class ExcelMonthImportHandler implements ExcelImportHandler {

    private static final Map<String, Month> MAP = new ConcurrentHashMap<>(16) {
        {
            put("一月", Month.JANUARY);
            put("二月", Month.FEBRUARY);
            put("三月", Month.MARCH);
            put("四月", Month.APRIL);
            put("五月", Month.MAY);
            put("六月", Month.JUNE);
            put("七月", Month.JULY);
            put("八月", Month.AUGUST);
            put("九月", Month.SEPTEMBER);
            put("十月", Month.OCTOBER);
            put("十一月", Month.NOVEMBER);
            put("十二月", Month.DECEMBER);
        }
    };

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        Month value = MAP.get(str);
        if (value == null) {
            return;
        }
        this.setValue(result, field, value);
    }

}
