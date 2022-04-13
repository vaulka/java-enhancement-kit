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

}
