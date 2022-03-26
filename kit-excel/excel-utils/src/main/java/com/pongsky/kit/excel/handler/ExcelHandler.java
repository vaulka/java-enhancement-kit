package com.pongsky.kit.excel.handler;

import com.pongsky.kit.excel.annotation.Excel;
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
     * @param excel 导出 excel 相关信息
     * @param obj   obj
     * @param info  导出 excel 相关参数信息
     * @throws IOException IOException
     */
    void exec(Field field, Excel excel, Object obj, ExcelExportInfo info) throws IOException;

}
