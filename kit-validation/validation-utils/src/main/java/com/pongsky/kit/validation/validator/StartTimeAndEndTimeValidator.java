package com.pongsky.kit.validation.validator;


import com.pongsky.kit.type.parser.enums.FieldType;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
import com.pongsky.kit.validation.annotation.validator.StartTimeAndEndTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;

/**
 * 校验开始时间和结束时间 校验器
 *
 * @author pengsenhao
 **/
public class StartTimeAndEndTimeValidator implements ConstraintValidator<StartTimeAndEndTime, Object> {

    /**
     * 开始时间字段名称
     */
    private String startTimeFieldName;

    /**
     * 开始时间名称
     */
    private String startTimeName;

    /**
     * 结束时间字段名称
     */
    private String endTimeFieldName;

    /**
     * 结束时间名称
     */
    private String endTimeName;

    /**
     * 是否可以相等
     */
    private boolean canBeEquals;

    /**
     * 是否可以为空
     */
    private boolean canBeNull;

    @Override
    public void initialize(StartTimeAndEndTime constraintAnnotation) {
        startTimeFieldName = constraintAnnotation.startTimeFieldName();
        startTimeName = constraintAnnotation.startTimeName();
        endTimeFieldName = constraintAnnotation.endTimeFieldName();
        endTimeName = constraintAnnotation.endTimeName();
        canBeEquals = constraintAnnotation.canBeEquals();
        canBeNull = constraintAnnotation.canBeNull();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            this.setErrorMessage(context, "校验对象不能为空");
            return false;
        }
        Field startTimeField, endTimeField;
        Object startTime, endTime;
        try {
            startTimeField = this.getField(value, startTimeFieldName, startTimeName);
            startTime = this.getTime(value, startTimeField, startTimeName);
            endTimeField = this.getField(value, endTimeFieldName, startTimeName);
            endTime = this.getTime(value, endTimeField, endTimeName);
        } catch (ValidationException e) {
            this.setErrorMessage(context, e.getLocalizedMessage());
            return false;
        }
        if (startTime == null || endTime == null) {
            return true;
        }
        FieldType startTimeType = FieldType.getType(startTimeField);
        FieldType endTimeType = FieldType.getType(endTimeField);
        if (startTimeType != endTimeType) {
            this.setErrorMessage(context, MessageFormat.format("{0} 与 {1} 的时间类型不一致", startTimeName, endTimeName));
            return false;
        }
        boolean isEquals;
        int compareTo;
        switch (startTimeType) {
            case DATE: {
                Date start = (Date) startTime;
                Date end = (Date) endTime;
                isEquals = start.equals(end);
                compareTo = start.compareTo(end);
                break;
            }
            case LOCAL_DATE: {
                LocalDate start = (LocalDate) startTime;
                LocalDate end = (LocalDate) endTime;
                isEquals = start.equals(end);
                compareTo = start.compareTo(end);
                break;
            }
            case LOCAL_TIME: {
                LocalTime start = (LocalTime) startTime;
                LocalTime end = (LocalTime) endTime;
                isEquals = start.equals(end);
                compareTo = start.compareTo(end);
                break;
            }
            case LOCAL_DATE_TIME: {
                LocalDateTime start = (LocalDateTime) startTime;
                LocalDateTime end = (LocalDateTime) endTime;
                isEquals = start.equals(end);
                compareTo = start.compareTo(end);
                break;
            }
            default:
                this.setErrorMessage(context, MessageFormat.format("校验时间不支持 {0} 数据类型", startTimeType.getType()));
                return false;
        }
        if (!canBeEquals && isEquals) {
            this.setErrorMessage(context, MessageFormat.format("{0} 与 {1} 不能相同", startTimeName, endTimeName));
            return false;
        }
        if (compareTo > 0) {
            this.setErrorMessage(context, MessageFormat.format("{0} 不能大于 {1}", startTimeName, endTimeName));
            return false;
        }
        return true;
    }

    /**
     * 获取时间
     *
     * @param value     对象
     * @param fieldName 字段名称
     * @param name      名称
     * @return 获取时间
     * @throws ValidationException 校验异常
     */
    private Field getField(Object value, String fieldName, String name) {
        Field field = Arrays.stream(value.getClass().getDeclaredFields())
                .filter(f -> f.getName().equals(fieldName))
                .findFirst()
                .orElse(null);
        if (field == null) {
            throw new ValidationException(MessageFormat.format("{0} 字段不存在", name));
        }
        return field;
    }

    /**
     * 获取时间
     *
     * @param value 对象
     * @param field 字段
     * @param name  名称
     * @return 获取时间
     * @throws ValidationException 校验异常
     */
    private Object getTime(Object value, Field field, String name) {
        Object time = ReflectUtils.getValue(value, field);
        if (time == null && !canBeNull) {
            throw new ValidationException(MessageFormat.format("{0} 不能为空", name));
        }
        return time;
    }

    /**
     * 设置错误信息
     *
     * @param context context
     * @param message 错误信息
     */
    private void setErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

}
