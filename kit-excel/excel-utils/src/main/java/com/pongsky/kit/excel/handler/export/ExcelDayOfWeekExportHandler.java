package com.pongsky.kit.excel.handler.export;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

/**
 * DayOfWeek 处理器
 *
 * @author pengsenhao
 **/
public class ExcelDayOfWeekExportHandler implements ExcelExportHandler {

    private static final Map<DayOfWeek, String> MAP = new HashMap<DayOfWeek, String>(10) {
        {
            put(DayOfWeek.MONDAY, "星期一");
            put(DayOfWeek.TUESDAY, "星期二");
            put(DayOfWeek.WEDNESDAY, "星期三");
            put(DayOfWeek.THURSDAY, "星期四");
            put(DayOfWeek.FRIDAY, "星期五");
            put(DayOfWeek.SATURDAY, "星期六");
            put(DayOfWeek.SUNDAY, "星期日");
        }
    };

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excelProperty.contentStyle().defaultValue()
                : MAP.get((DayOfWeek) obj);
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
