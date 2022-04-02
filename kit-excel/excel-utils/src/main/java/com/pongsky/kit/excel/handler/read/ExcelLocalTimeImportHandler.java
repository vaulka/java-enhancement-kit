package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalTime 处理器
 *
 * @author pengsenhao
 **/
public class ExcelLocalTimeImportHandler implements ExcelImportHandler {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        if (StringUtils.isBlank(str)) {
            return;
        }
        LocalTime value = LocalTime.parse(str, FORMAT);
        this.setValue(result, field, value);
    }

}
