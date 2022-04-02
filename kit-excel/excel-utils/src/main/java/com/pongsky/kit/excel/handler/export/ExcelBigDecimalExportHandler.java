package com.pongsky.kit.excel.handler.export;

import com.pongsky.kit.excel.annotation.ExcelProperty;
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
public class ExcelBigDecimalExportHandler implements ExcelExportHandler {

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) {
        String value = obj == null
                ? excelProperty.defaultValue()
                : ((BigDecimal) obj).toPlainString();
        value = StringUtils.isNotBlank(excelProperty.suffix())
                ? value + excelProperty.suffix()
                : value;
        info.getCell().setCellValue(new XSSFRichTextString(value));
        info.setTextWidth(info.getCell().getColumnIndex(), value.length());
    }

}
