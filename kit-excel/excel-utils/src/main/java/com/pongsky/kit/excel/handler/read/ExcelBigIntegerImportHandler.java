package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * BigInteger 处理器
 *
 * @author pengsenhao
 **/
public class ExcelBigIntegerImportHandler implements ExcelImportHandler {

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.contentStyle().defaultValue();
        int lastIndexOf = str.lastIndexOf(excelProperty.contentStyle().suffix());
        str = StringUtils.isNotBlank(excelProperty.contentStyle().suffix()) && lastIndexOf != -1
                ? str.substring(0, lastIndexOf)
                : str;
        if (StringUtils.isBlank(str)) {
            return;
        }
        BigInteger value = new BigInteger(str);
        ReflectUtils.setValue(result, field, value);
    }

}
