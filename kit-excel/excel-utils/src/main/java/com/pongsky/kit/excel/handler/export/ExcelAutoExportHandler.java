package com.pongsky.kit.excel.handler.export;

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
public class ExcelAutoExportHandler implements ExcelExportHandler {

    @Override
    public void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) {
        ParseType type = ParseType.getFieldType(field);
        if (type.getExportHandler() == null) {
            return;
        }
        try {
            type.getExportHandler().getDeclaredConstructor().newInstance()
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
