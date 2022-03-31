package com.pongsky.kit.excel.annotation;

import com.pongsky.kit.excel.handler.ExcelAutoHandler;
import com.pongsky.kit.excel.handler.ExcelHandler;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

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
     * 列名
     * <p>
     * 不填写则默认获取字段名
     *
     * @return 列名
     */
    String[] value() default {};

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
     * 列值处理器，默认根据字段类型自动选择转换器
     * <p>
     * 如果自定义的话，需注意如下：
     * 1、列值需手动设置
     * 2、宽度、高度需手动设置，否则无法自适应
     *
     * @return 列值处理器
     */
    Class<? extends ExcelHandler> handler() default ExcelAutoHandler.class;

    /**
     * 列值默认值
     * <p>
     * 目前仅使用 {@link com.pongsky.kit.excel.handler.ExcelStringHandler} 处理器才生效
     * 可自定义 handler 进行设置
     * <p>
     * 如果空数据，则显示默认值
     *
     * @return 列值默认值
     */
    String defaultValue() default "";

    /**
     * 列值后缀
     * <p>
     * 目前仅使用 {@link com.pongsky.kit.excel.handler.ExcelStringHandler} 处理器才生效
     * 可自定义 handler 进行设置
     * <p>
     * 譬如：
     * 数据 90，后缀 %
     * <p>
     * 90 &gt; 90%
     *
     * @return 列值后缀
     */
    String suffix() default "";

    /**
     * 数据格式
     * <p>
     * 填写参数查阅 {@link org.apache.poi.ss.usermodel.BuiltinFormats#_formats}
     *
     * @return 数据格式
     */
    String dataFormat() default "@";

    /**
     * 列排序值
     * <p>
     * 值越大排越后面
     *
     * @return 列排序值
     */
    int sort() default 0;

    /**
     * 列名 超链接
     * <p>
     * 目前仅支持 URL 超链接
     *
     * @return 列名 超链接
     */
    String hyperlink() default "";

    /**
     * 列名 批注
     *
     * @return 列名 批注
     */
    String comment() default "";

    /**
     * 列名 背景颜色
     *
     * @return 列名 背景颜色
     */
    IndexedColors backgroundColor() default IndexedColors.PALE_BLUE;

    /**
     * 列名 字体颜色
     *
     * @return 列名 字体颜色
     */
    IndexedColors fontColor() default IndexedColors.BLACK;

    /**
     * 字体
     *
     * @return 字体
     */
    String fontName() default "Arial";

    /**
     * 字号
     *
     * @return 字号
     */
    short fontSize() default 11;

    /**
     * 列名 边框样式
     *
     * @return 列名 边框样式
     */
    BorderStyle borderStyle() default BorderStyle.MEDIUM;

    /**
     * 列名 边框颜色
     *
     * @return 边框颜色
     */
    IndexedColors borderColor() default IndexedColors.WHITE;

    /**
     * 水平对齐方式
     *
     * @return 水平对齐方式
     */
    HorizontalAlignment alignment() default HorizontalAlignment.CENTER;

    /**
     * 垂直对齐方式
     *
     * @return 垂直对齐方式
     */
    VerticalAlignment verticalAlignment() default VerticalAlignment.CENTER;

}
