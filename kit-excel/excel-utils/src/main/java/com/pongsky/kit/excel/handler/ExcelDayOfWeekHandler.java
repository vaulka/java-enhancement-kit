package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.util.Map;

/**
 * DayOfWeek 处理器
 *
 * @author pengsenhao
 **/
public class ExcelDayOfWeekHandler implements ExcelHandler {

    private static final Map<DayOfWeek, String> DAY_OF_WEEK_STRING_MAP = Map.of(
            DayOfWeek.MONDAY, "星期一",
            DayOfWeek.TUESDAY, "星期二",
            DayOfWeek.WEDNESDAY, "星期三",
            DayOfWeek.THURSDAY, "星期四",
            DayOfWeek.FRIDAY, "星期五",
            DayOfWeek.SATURDAY, "星期六",
            DayOfWeek.SUNDAY, "星期日"
    );

    @Override
    public void exec(Field field, Excel excel, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excel.defaultValue()
                : DAY_OF_WEEK_STRING_MAP.get((DayOfWeek) obj);
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
