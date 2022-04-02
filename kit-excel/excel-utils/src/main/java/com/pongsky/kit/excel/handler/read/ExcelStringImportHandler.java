package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
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
                : excelProperty.defaultValue();
        String value = StringUtils.isNotBlank(excelProperty.suffix())
                ? str.substring(0, str.lastIndexOf(excelProperty.suffix()))
                : str;
        this.setValue(result, field, value);
    }

}
