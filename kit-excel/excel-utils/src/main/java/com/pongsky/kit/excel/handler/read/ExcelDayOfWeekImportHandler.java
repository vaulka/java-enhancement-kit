package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DayOfWeek 处理器
 *
 * @author pengsenhao
 **/
public class ExcelDayOfWeekImportHandler implements ExcelImportHandler {

    private static final Map<String, DayOfWeek> MAP = new ConcurrentHashMap<String, DayOfWeek>(10) {
        private static final long serialVersionUID = -1539757981111008963L;

        {
            put("星期一", DayOfWeek.MONDAY);
            put("星期二", DayOfWeek.TUESDAY);
            put("星期三", DayOfWeek.WEDNESDAY);
            put("星期四", DayOfWeek.THURSDAY);
            put("星期五", DayOfWeek.FRIDAY);
            put("星期六", DayOfWeek.SATURDAY);
            put("星期日", DayOfWeek.SUNDAY);
        }
    };

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.contentStyle().defaultValue();
        DayOfWeek value = MAP.get(str);
        if (value == null) {
            return;
        }
        ReflectUtils.setValue(result, field, value);
    }

}
