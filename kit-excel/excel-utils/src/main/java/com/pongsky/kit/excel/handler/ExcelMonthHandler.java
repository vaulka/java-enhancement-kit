package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

/**
 * Month 处理器
 *
 * @author pengsenhao
 **/
public class ExcelMonthHandler implements ExcelHandler {

    private static final Map<Month, String> MONTH_STRING_MAP = new HashMap<>(16) {
        {
            put(Month.JANUARY, "一月");
            put(Month.FEBRUARY, "二月");
            put(Month.MARCH, "三月");
            put(Month.APRIL, "四月");
            put(Month.MAY, "五月");
            put(Month.JUNE, "六月");
            put(Month.JULY, "七月");
            put(Month.AUGUST, "八月");
            put(Month.SEPTEMBER, "九月");
            put(Month.OCTOBER, "十月");
            put(Month.NOVEMBER, "十一月");
            put(Month.DECEMBER, "十二月");
        }
    };

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excelProperty.defaultValue()
                : MONTH_STRING_MAP.get((Month) obj);
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
