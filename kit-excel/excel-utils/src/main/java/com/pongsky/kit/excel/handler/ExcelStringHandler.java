package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;

/**
 * String 处理器
 *
 * @author pengsenhao
 **/
public class ExcelStringHandler implements ExcelHandler {

    @Override
    public void exec(Field field, Excel excel, Object obj, ExcelExportInfo info) {
        // 不能使用 (String)obj
        // 原因是未知类型以及一些其他类型默认也是使用此 handler 进行处理，无法转为 String，故使用 toString()
        if (obj == null || StringUtils.isBlank(obj.toString())) {
            obj = excel.defaultValue();
        }
        String value = StringUtils.isNotBlank(excel.suffix())
                ? obj + excel.suffix()
                : obj.toString();
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
