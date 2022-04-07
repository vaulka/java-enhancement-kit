package com.pongsky.kit.excel.handler.export;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;

/**
 * String 处理器
 *
 * @author pengsenhao
 **/
public class ExcelStringExportHandler implements ExcelExportHandler {

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) {
        // 不能使用 (String)obj
        // 原因是未知类型以及一些其他类型默认也是使用此 handler 进行处理，无法转为 String，故使用 toString()
        if (obj == null || StringUtils.isBlank(obj.toString())) {
            obj = excelProperty.contentStyle().defaultValue();
        }
        String value = StringUtils.isNotBlank(excelProperty.contentStyle().suffix())
                ? obj + excelProperty.contentStyle().suffix()
                : obj.toString();
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
