package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate 处理器
 *
 * @author pengsenhao
 **/
public class ExcelLocalDateImportHandler implements ExcelImportHandler {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        if (obj instanceof LocalDate) {
            // 如果是日期类型，则直接处理
            this.setValue(result, field, obj);
            return;
        }
        // 如果不是日期类型，则默认按照字符串类型处理
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        if (StringUtils.isBlank(str)) {
            return;
        }
        LocalDate value = LocalDate.parse(str, FORMAT);
        this.setValue(result, field, value);
    }

}
