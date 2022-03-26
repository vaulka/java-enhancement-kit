package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Integer Week 处理器
 *
 * @author pengsenhao
 **/
public class ExcelIntegerWeekHandler implements ExcelHandler {

    private static final Map<Integer, String> INTEGER_WEEK_STRING_MAP = Map.of(
            1, "星期一",
            2, "星期二",
            3, "星期三",
            4, "星期四",
            5, "星期五",
            6, "星期六",
            7, "星期日"
    );

    @Override
    public void exec(Field field, Excel excel, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excel.defaultValue()
                : INTEGER_WEEK_STRING_MAP.get((Integer) obj);
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
