package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.util.Map;

/**
 * DayOfWeek 处理器
 *
 * @author pengsenhao
 **/
public class ExcelDayOfWeekImportHandler implements ExcelImportHandler {

    private static final Map<String, DayOfWeek> DAY_OF_WEEK_MAP = Map.of(
            "星期一", DayOfWeek.MONDAY,
            "星期二", DayOfWeek.TUESDAY,
            "星期三", DayOfWeek.WEDNESDAY,
            "星期四", DayOfWeek.THURSDAY,
            "星期五", DayOfWeek.FRIDAY,
            "星期六", DayOfWeek.SATURDAY,
            "星期日", DayOfWeek.SUNDAY
    );

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        DayOfWeek value = DAY_OF_WEEK_MAP.get(str);
        if (value == null) {
            return;
        }
        this.setValue(result, field, value);
    }

}
