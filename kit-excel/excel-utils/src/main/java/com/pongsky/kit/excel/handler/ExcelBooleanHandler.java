package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Boolean 处理器
 *
 * @author pengsenhao
 **/
public class ExcelBooleanHandler implements ExcelHandler {

    private static final Map<Boolean, String> BOOLEAN_STRING_MAP = Map.of(
            Boolean.TRUE, "是",
            Boolean.FALSE, "否"
    );

    @Override
    public void exec(Field field, Excel excel, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excel.defaultValue()
                : BOOLEAN_STRING_MAP.get((Boolean) obj);
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
