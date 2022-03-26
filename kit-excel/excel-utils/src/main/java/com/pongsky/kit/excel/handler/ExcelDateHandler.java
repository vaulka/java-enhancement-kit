package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Date 处理器
 *
 * @author pengsenhao
 **/
public class ExcelDateHandler implements ExcelHandler {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void exec(Field field, Excel excel, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excel.defaultValue()
                : DateFormatUtils.format((Date) obj, PATTERN);
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
