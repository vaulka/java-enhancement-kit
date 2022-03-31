package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import com.pongsky.kit.excel.enums.ParseType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 自动转换 处理器
 *
 * @author pengsenhao
 **/
public class ExcelAutoHandler implements ExcelHandler {

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) {
        ParseType type = ParseType.getFieldType(field);
        try {
            type.getHandler().getDeclaredConstructor().newInstance()
                    .exec(field, excelProperty, obj, info);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException
                | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

}
