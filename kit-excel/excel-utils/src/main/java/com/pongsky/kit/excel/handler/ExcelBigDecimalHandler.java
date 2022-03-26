package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * BigDecimal 处理器
 *
 * @author pengsenhao
 **/
public class ExcelBigDecimalHandler implements ExcelHandler {

    @Override
    public void exec(Field field, Excel excel, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excel.defaultValue()
                : ((BigDecimal) obj).toPlainString();
        value = StringUtils.isNotBlank(excel.suffix())
                ? value + excel.suffix()
                : value;
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
