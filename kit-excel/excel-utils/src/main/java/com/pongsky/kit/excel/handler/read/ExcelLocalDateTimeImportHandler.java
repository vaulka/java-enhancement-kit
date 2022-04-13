package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime 处理器
 *
 * @author pengsenhao
 **/
public class ExcelLocalDateTimeImportHandler implements ExcelImportHandler {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        if (obj instanceof LocalDateTime) {
            // 如果是日期类型，则直接处理
            ReflectUtils.setValue(result, field, obj);
            return;
        }
        // 如果不是日期类型，则默认按照字符串类型处理
        String str = obj != null
                ? obj.toString()
                : excelProperty.contentStyle().defaultValue();
        if (StringUtils.isBlank(str)) {
            return;
        }
        LocalDateTime value = LocalDateTime.parse(str, FORMAT);
        ReflectUtils.setValue(result, field, value);
    }

}
