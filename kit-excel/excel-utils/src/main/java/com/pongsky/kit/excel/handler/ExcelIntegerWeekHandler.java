package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Integer Week 处理器
 *
 * @author pengsenhao
 **/
public class ExcelIntegerWeekHandler implements ExcelHandler {

    private static final Map<Integer, String> INTEGER_WEEK_STRING_MAP = new HashMap<Integer, String>() {{
        put(1, "星期一");
        put(2, "星期二");
        put(3, "星期三");
        put(4, "星期四");
        put(5, "星期五");
        put(6, "星期六");
        put(7, "星期日");
    }};

    @Override
    public void exec(Field field, Excel excel, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excel.defaultValue()
                : INTEGER_WEEK_STRING_MAP.get((Integer) obj);
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
