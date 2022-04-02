package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;

/**
 * Date 处理器
 *
 * @author pengsenhao
 **/
public class ExcelDateImportHandler implements ExcelImportHandler {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IllegalAccessException {
        String str = obj != null
                ? obj.toString()
                : excelProperty.defaultValue();
        if (StringUtils.isBlank(str)) {
            return;
        }
        Date value;
        try {
            value = DateUtils.parseDate(str, PATTERN);
        } catch (ParseException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        this.setValue(result, field, value);
    }

}
