package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Double 处理器
 *
 * @author pengsenhao
 **/
public class ExcelDoubleImportHandler implements ExcelImportHandler {

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        int lastIndexOf = str.lastIndexOf(excelProperty.suffix());
        str = StringUtils.isNotBlank(excelProperty.suffix()) && lastIndexOf != -1
                ? str.substring(0, lastIndexOf)
                : str;
        if (StringUtils.isBlank(str)) {
            return;
        }
        Double value = Double.parseDouble(str);
        this.setValue(result, field, value);
    }

}
