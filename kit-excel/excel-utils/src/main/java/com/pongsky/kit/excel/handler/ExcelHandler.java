package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.entity.ExcelExportInfo;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 列值处理器
 *
 * @author pengsenhao
 **/
public interface ExcelHandler {

    /**
     * 处理列值数据
     *
     * @param field field
     * @param excelProperty 导出 excel 相关信息
     * @param obj   obj
     * @param info  导出 excel 相关参数信息
     * @throws IOException IOException
     */
    void exec(Field field, ExcelProperty excelProperty, Object obj, ExcelExportInfo info) throws IOException;

}
