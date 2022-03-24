package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * 字符串 处理器
 *
 * @author pengsenhao
 **/
public class ExcelStringHandler implements ExcelHandler {

    @Override
    public void exec(Excel excel, Object obj, ExcelExportInfo<?> info) {
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
