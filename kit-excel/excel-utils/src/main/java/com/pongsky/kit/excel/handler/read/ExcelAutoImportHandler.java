package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.enums.ParseType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 自动转换 处理器
 *
 * @author pengsenhao
 **/
public class ExcelAutoImportHandler implements ExcelImportHandler {

    @Override
    public void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws ReflectiveOperationException {
        ParseType type = ParseType.getFieldType(field);
        if (type.getImportHandler() == null) {
            return;
        }
        try {
            type.getImportHandler().getDeclaredConstructor().newInstance()
                    .exec(result, field, excelProperty, obj);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException
                | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

}
