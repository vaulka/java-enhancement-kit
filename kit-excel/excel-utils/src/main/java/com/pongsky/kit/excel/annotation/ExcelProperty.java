package com.pongsky.kit.excel.annotation;

import com.pongsky.kit.excel.annotation.style.ExcelContentStyle;
import com.pongsky.kit.excel.annotation.style.ExcelHeadStyle;
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
     * Y轴 列名
     * <p>
     * 不填写则默认获取字段名
     *
     * @return Y轴 列名
     */
    String[] yHead() default {};

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
    String attrs() default "";

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
     * 自定义 X轴 标题样式
     *
     * @return 自定义 X轴 标题样式
     */
    ExcelHeadStyle xHeadStyle() default @ExcelHeadStyle;

    /**
     * 自定义 Y轴 标题样式
     *
     * @return 自定义 Y轴 标题样式
     */
    ExcelHeadStyle yHeadStyle() default @ExcelHeadStyle;

    /**
     * 自定义 内容样式
     *
     * @return 自定义 内容样式
     */
    ExcelContentStyle contentStyle() default @ExcelContentStyle;

}
