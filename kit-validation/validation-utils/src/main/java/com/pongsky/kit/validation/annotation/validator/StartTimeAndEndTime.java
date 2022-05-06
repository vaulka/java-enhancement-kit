package com.pongsky.kit.validation.annotation.validator;

import com.pongsky.kit.validation.validator.StartTimeAndEndTimeValidator;

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
 * 校验开始时间和结束时间
 * <p>
 * 适用于以下时间类型：
 * <ul>
 * <li>{@link java.util.Date}</li>
 * <li>{@link java.time.LocalDate}</li>
 * <li>{@link java.time.LocalTime}</li>
 * <li>{@link java.time.LocalDateTime}</li>
 * </ul>
 *
 * @author pengsenhao
 **/
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(StartTimeAndEndTime.List.class)
@Constraint(validatedBy = StartTimeAndEndTimeValidator.class)
public @interface StartTimeAndEndTime {

    /**
     * 开始时间字段名称
     *
     * @return 开始时间字段名称
     */
    String startTimeFieldName() default "startTime";

    /**
     * 开始时间名称
     *
     * @return 开始时间名称
     */
    String startTimeName() default "开始时间";

    /**
     * 结束时间字段名称
     *
     * @return 结束时间字段名称
     */
    String endTimeFieldName() default "endTime";

    /**
     * 结束时间名称
     *
     * @return 结束时间名称
     */
    String endTimeName() default "结束时间";

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
         * 校验开始时间和结束时间列表
         *
         * @return 校验开始时间和结束时间列表
         */
        StartTimeAndEndTime[] value();

    }

}
