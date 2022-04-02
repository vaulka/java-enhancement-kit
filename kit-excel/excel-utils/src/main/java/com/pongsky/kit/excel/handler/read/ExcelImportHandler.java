package com.pongsky.kit.excel.handler.read;

import com.pongsky.kit.excel.annotation.ExcelProperty;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 导入 列值处理器
 *
 * @author pengsenhao
 **/
public interface ExcelImportHandler {

    /**
     * 处理列值数据
     *
     * @param result        数据实体
     * @param field         field
     * @param excelProperty 导出 excel 相关信息
     * @param obj           obj
     * @throws IOException                  IOException
     * @throws ReflectiveOperationException ReflectiveOperationException
     */
    void exec(Object result, Field field, ExcelProperty excelProperty, Object obj) throws IOException, ReflectiveOperationException;

    /**
     * 设置属性值
     *
     * @param obj    obj
     * @param field  字段
     * @param result 值
     * @throws IllegalAccessException IllegalAccessException
     * @author pengsenhao
     */
    default void setValue(Object obj, Field field, Object result) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(obj, result);
    }

}
