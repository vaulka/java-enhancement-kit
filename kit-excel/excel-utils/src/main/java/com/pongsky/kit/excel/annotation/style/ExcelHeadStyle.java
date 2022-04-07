package com.pongsky.kit.excel.annotation.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 标题样式
 *
 * @author pengsenhao
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHeadStyle {

    /**
     * 数据格式
     * <p>
     * 填写参数查阅 {@link org.apache.poi.ss.usermodel.BuiltinFormats#_formats}
     *
     * @return 数据格式
     */
    String dataFormat() default "@";

    /**
     * 超链接
     * <p>
     * 目前仅支持 URL 超链接
     *
     * @return 超链接
     */
    String hyperlink() default "";

    /**
     * 批注
     *
     * @return 批注
     */
    String comment() default "";

    /**
     * 是否粗体
     *
     * @return 是否粗体
     */
    boolean isBold() default true;

    /**
     * 是否锁定
     *
     * @return 是否锁定
     */
    boolean isLocked() default false;

    /**
     * 背景颜色
     *
     * @return 背景颜色
     */
    IndexedColors backgroundColor() default IndexedColors.PALE_BLUE;

    /**
     * 上边框样式
     *
     * @return 底边框样式
     */
    BorderStyle borderTop() default BorderStyle.THIN;

    /**
     * 下边框样式
     *
     * @return 下边框样式
     */
    BorderStyle borderBottom() default BorderStyle.THIN;

    /**
     * 左边框样式
     *
     * @return 左边框样式
     */
    BorderStyle borderLeft() default BorderStyle.THIN;

    /**
     * 右边框样式
     *
     * @return 右边框样式
     */
    BorderStyle borderRight() default BorderStyle.THIN;

    /**
     * 上边框颜色
     *
     * @return 边框颜色
     */
    IndexedColors borderTopColor() default IndexedColors.BLACK;

    /**
     * 下边框颜色
     *
     * @return 下边框颜色
     */
    IndexedColors borderBottomColor() default IndexedColors.BLACK;

    /**
     * 左边框颜色
     *
     * @return 左边框颜色
     */
    IndexedColors borderLeftColor() default IndexedColors.BLACK;

    /**
     * 右边框颜色
     *
     * @return 右边框颜色
     */
    IndexedColors borderRightColor() default IndexedColors.BLACK;

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
     * 字体颜色
     *
     * @return 字体颜色
     */
    IndexedColors fontColor() default IndexedColors.BLACK;

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
