package com.pongsky.kit.excel.annotation;

import com.pongsky.kit.excel.annotation.content.ExcelContentStyle;
import com.pongsky.kit.excel.annotation.header.ExcelHeadAttr;
import com.pongsky.kit.excel.annotation.header.ExcelHeadStyle;
import com.pongsky.kit.excel.handler.export.ExcelAutoExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelExportHandler;
import com.pongsky.kit.excel.handler.read.ExcelAutoImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelImportHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 信息
 *
 * @author pengsenhao
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelProperty {

    /**
     * 是否导出数据
     *
     * @return 是否导出数据
     */
    boolean isExportData() default true;

    /**
     * 顶部 列名
     * <p>
     * 不填写则默认获取字段名
     *
     * @return 顶部 列名
     */
    String[] topHeads() default {};

    /**
     * 顶部 列名属性信息列表
     *
     * @return 顶部 列名属性信息列表
     */
    ExcelHeadAttr[] topHeadAttrs() default {};

    /**
     * 左部 列名
     * <p>
     * 不填写则默认获取字段名
     *
     * @return 左部 列名
     */
    String[] leftHeads() default {};

    /**
     * 左部 列名属性信息列表
     *
     * @return 左部 列名属性信息列表
     */
    ExcelHeadAttr[] leftHeadAttrs() default {};

    /**
     * 获取多层级属性名称
     * <p>
     * 以 . 间隔，接着获取 List、Set、Map 数据以 [x] 做间隔
     * <p>
     * 譬如：
     * name
     * order.user[1].name
     *
     * @return 枚举格式
     */
    String attr() default "";

    /**
     * 导出 列值处理器，默认根据字段类型自动选择转换器
     * <p>
     * 如果自定义的话，需注意如下：
     * 1、列值需手动设置
     * 2、宽度、高度需手动设置，否则无法自适应
     *
     * @return 列值处理器
     */
    Class<? extends ExcelExportHandler> exportHandler() default ExcelAutoExportHandler.class;

    /**
     * 导入 列值处理器，默认根据字段类型自动选择转换器
     *
     * @return 列值处理器
     */
    Class<? extends ExcelImportHandler> importHandler() default ExcelAutoImportHandler.class;

    /**
     * 列排序值
     * <p>
     * 值越大排越后面
     *
     * @return 列排序值
     */
    int sort() default 0;

    /**
     * 顶部 标题样式
     *
     * @return 顶部 标题样式
     */
    ExcelHeadStyle topHeadStyle() default @ExcelHeadStyle;

    /**
     * 左部 标题样式
     *
     * @return 左部 标题样式
     */
    ExcelHeadStyle leftHeadStyle() default @ExcelHeadStyle;

    /**
     * 自定义 内容样式
     *
     * @return 自定义 内容样式
     */
    ExcelContentStyle contentStyle() default @ExcelContentStyle;

}
