package com.pongsky.kit.excel.annotation;

import com.pongsky.kit.excel.handler.ExcelHandler;
import com.pongsky.kit.excel.handler.ExcelStringHandler;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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
public @interface Excel {

    /**
     * 列名
     * <p>
     * 不填写则默认获取字段名
     *
     * @return 列名
     */
    String name() default "";

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
     * 列值处理器
     * <p>
     * 如果自定义的话，需注意如下：
     * 1、 {@link Excel#defaultValue()}、{@link Excel#suffix()} 需手动设置
     * 2、列值需手动设置
     * 3、宽度、高度需手动设置
     *
     * @return 列值处理器
     */
    Class<? extends ExcelHandler> handler() default ExcelStringHandler.class;

    /**
     * 列值默认值
     * <p>
     * 如果空数据，则显示默认值
     *
     * @return 列值默认值
     */
    String defaultValue() default "";

    /**
     * 列值后缀（需结合 {@link com.pongsky.kit.excel.handler.ExcelStringHandler} 使用或自定义 handler 进行设置）
     * <p>
     * 譬如：
     * 数据 90，后缀 %
     * <p>
     * 90 变更为 90%
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
     * 列名 批注
     *
     * @return 列名 批注
     */
    String comment() default "";

    /**
     * 列排序值
     * <p>
     * 值越大排越后面
     *
     * @return 列排序值
     */
    int sort() default 0;

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
