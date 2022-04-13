package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * String 处理器
 *
 * @author pengsenhao
 **/
public class ExcelStringImportHandler implements ExcelImportHandler {

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.contentStyle().defaultValue();
        String value = StringUtils.isNotBlank(excelProperty.contentStyle().suffix())
                ? str.substring(0, str.lastIndexOf(excelProperty.contentStyle().suffix()))
                : str;
        ReflectUtils.setValue(result, field, value);
    }

}
