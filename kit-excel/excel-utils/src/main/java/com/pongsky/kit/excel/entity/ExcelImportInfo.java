package com.pongsky.kit.excel.entity;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 导入 excel 相关参数信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ExcelImportInfo {

    /**
     * field 与 excel 列表
     * <p>
     * List<List<Field,Excel>>
     */
    private List<List<Object>> fields;

    /**
     * field 下标
     */
    private static final int FIELD_INDEX = 0;

    /**
     * excel 下标
     */
    private static final int EXCEL_INDEX = 1;

    /**
     * 获取 field 信息
     *
     * @param fieldExcels fieldExcels
     * @return 获取 field 信息
     */
    public static Field getField(List<Object> fieldExcels) {
        return (Field) fieldExcels.get(FIELD_INDEX);
    }

    /**
     * 获取 excel 信息
     *
     * @param fieldExcels fieldExcels
     * @return 获取 excel 信息
     */
    public static ExcelProperty getExcel(List<Object> fieldExcels) {
        return (ExcelProperty) fieldExcels.get(EXCEL_INDEX);
    }

    /**
     * 数据实体类
     */
    private Class<?> clazz;

    /**
     * 列名最大行数，从 0 开始
     */
    private int titleMaxNum;

}
