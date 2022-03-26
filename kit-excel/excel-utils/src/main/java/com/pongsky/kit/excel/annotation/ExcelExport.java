package com.pongsky.kit.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 导出信息
 * <p>
 * 标记该接口支持 excel 导出
 * <p>
 * 注意：返回的类型必须是 List，如果是多层级下，请设置 {@link ExcelExport#attrs()} 进行获取
 *
 * @author pengsenhao
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {

    /**
     * 返回的数据类信息
     *
     * @return 返回的数据类信息
     */
    Class<?> value();

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
     * 工作表名称
     *
     * @return 工作表名称
     */
    String sheetName() default "";

    /**
     * excel 文件名称
     *
     * @return excel 文件名称
     */
    String fileName() default "export";

}
