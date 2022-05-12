package com.pongsky.kit.validation.annotation.validator;

import com.pongsky.kit.validation.validator.MinNumAndMaxNumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验最小数值以及最大数值
 * <p>
 * 适用于以下数值类型：
 * <ul>
 * <li>{@link java.lang.Float}</li>
 * <li>{@link java.lang.Double}</li>
 * <li>{@link java.lang.Byte}</li>
 * <li>{@link java.lang.Short}</li>
 * <li>{@link java.lang.Integer}</li>
 * <li>{@link java.lang.Long}</li>
 * <li>{@link java.math.BigInteger}</li>
 * <li>{@link java.math.BigDecimal}</li>
 * </ul>
 *
 * @author pengsenhao
 **/
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MinNumAndMaxNum.List.class)
@Constraint(validatedBy = MinNumAndMaxNumValidator.class)
public @interface MinNumAndMaxNum {

    /**
     * 最小数值字段名称
     *
     * @return 最小数值字段名称
     */
    String minNumFieldName() default "minNum";

    /**
     * 最小数值名称
     *
     * @return 最小数值名称
     */
    String minNumName() default "最小数值";

    /**
     * 最大数值字段名称
     *
     * @return 最大数值字段名称
     */
    String maxNumFieldName() default "maxNum";

    /**
     * 最大数值名称
     *
     * @return 最大数值名称
     */
    String maxNumName() default "最大数值名称";

    /**
     * 是否可以相等
     *
     * @return 是否可以相等
     */
    boolean canBeEquals() default false;

    /**
     * 是否可以为空
     *
     * @return 是否可以为空
     */
    boolean canBeNull() default true;

    /**
     * 信息
     *
     * @return 信息
     */
    String message() default "";

    /**
     * 组别
     *
     * @return 组别
     */
    Class<?>[] groups() default {};

    /**
     * 载体
     *
     * @return 载体
     */
    Class<? extends Payload>[] payload() default {};

    @Documented
    @Retention(RUNTIME)
    @Target({ElementType.TYPE})
    @interface List {

        /**
         * 校验最小数值以及最大数值列表
         *
         * @return 校验最小数值以及最大数值列表
         */
        MinNumAndMaxNum[] value();

    }

}
