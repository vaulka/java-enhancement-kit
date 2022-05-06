package com.pongsky.kit.validation.validator;


import com.pongsky.kit.type.parser.enums.FieldType;
import com.pongsky.kit.type.parser.utils.ReflectUtils;
import com.pongsky.kit.validation.annotation.validator.MinNumAndMaxNum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 校验最小数值以及最大数值 校验器
 *
 * @author pengsenhao
 **/
public class MinNumAndMaxNumValidator implements ConstraintValidator<MinNumAndMaxNum, Object> {

    /**
     * 最小数值字段名称
     */
    private String minNumFieldName;

    /**
     * 最小数值名称
     */
    private String minNumName;

    /**
     * 最大数值字段名称
     */
    private String maxNumFieldName;

    /**
     * 最大数值名称
     */
    private String maxNumName;

    /**
     * 是否可以相等
     */
    private boolean canBeEquals;

    /**
     * 是否可以为空
     */
    private boolean canBeNull;

    @Override
    public void initialize(MinNumAndMaxNum constraintAnnotation) {
        minNumFieldName = constraintAnnotation.minNumFieldName();
        minNumName = constraintAnnotation.minNumName();
        maxNumFieldName = constraintAnnotation.maxNumFieldName();
        maxNumName = constraintAnnotation.maxNumName();
        canBeEquals = constraintAnnotation.canBeEquals();
        canBeNull = constraintAnnotation.canBeNull();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            this.setErrorMessage(context, "校验对象不能为空");
            return false;
        }
        Field minNumField, maxNumField;
        Object minNum, maxNum;
        try {
            minNumField = this.getField(value, minNumFieldName, minNumName);
            minNum = this.getFieldValue(value, minNumField, minNumName);
            maxNumField = this.getField(value, maxNumFieldName, maxNumName);
            maxNum = this.getFieldValue(value, maxNumField, maxNumName);
        } catch (ValidationException e) {
            this.setErrorMessage(context, e.getLocalizedMessage());
            return false;
        }
        if (minNum == null || maxNum == null) {
            return true;
        }
        FieldType minNumType = FieldType.getType(minNumField);
        FieldType maxNumType = FieldType.getType(maxNumField);
        if (minNumType != maxNumType) {
            this.setErrorMessage(context, MessageFormat.format("{0} 与 {1} 的数值类型不一致", minNumName, maxNumName));
            return false;
        }
        boolean isEquals = minNum.equals(maxNum);
        int compareTo;
        switch (minNumType) {
            case FLOAT: {
                Float min = (Float) minNum;
                Float max = (Float) maxNum;
                compareTo = min.compareTo(max);
                break;
            }
            case DOUBLE: {
                Double min = (Double) minNum;
                Double max = (Double) maxNum;
                compareTo = min.compareTo(max);
                break;
            }
            case BYTE: {
                Byte min = (Byte) minNum;
                Byte max = (Byte) maxNum;
                compareTo = min.compareTo(max);
                break;
            }
            case SHORT: {
                Short min = (Short) minNum;
                Short max = (Short) maxNum;
                compareTo = min.compareTo(max);
                break;
            }
            case INTEGER: {
                Integer min = (Integer) minNum;
                Integer max = (Integer) maxNum;
                compareTo = min.compareTo(max);
                break;
            }
            case LONG: {
                Long min = (Long) minNum;
                Long max = (Long) maxNum;
                compareTo = min.compareTo(max);
                break;
            }
            case BIG_INTEGER: {
                BigInteger min = (BigInteger) minNum;
                BigInteger max = (BigInteger) maxNum;
                compareTo = min.compareTo(max);
                break;
            }
            case BIG_DECIMAL: {
                BigDecimal min = (BigDecimal) minNum;
                BigDecimal max = (BigDecimal) maxNum;
                compareTo = min.compareTo(max);
                break;
            }
            default:
                this.setErrorMessage(context, MessageFormat.format("校验数值不支持 {0} 数据类型", minNumType.getType()));
                return false;
        }
        if (!canBeEquals && isEquals) {
            this.setErrorMessage(context, MessageFormat.format("{0} 与 {1} 不能相同", minNumName, maxNumName));
            return false;
        }
        if (compareTo > 0) {
            this.setErrorMessage(context, MessageFormat.format("{0} 不能大于 {1}", minNumName, maxNumName));
            return false;
        }
        return true;
    }

    /**
     * 获取字段
     *
     * @param value     对象
     * @param fieldName 字段名称
     * @param name      名称
     * @return 获取字段
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
     * 获取字段值
     *
     * @param value 对象
     * @param field 字段
     * @param name  名称
     * @return 获取字段值
     * @throws ValidationException 校验异常
     */
    private Object getFieldValue(Object value, Field field, String name) {
        Object fieldValue = ReflectUtils.getValue(value, field);
        if (fieldValue == null && !canBeNull) {
            throw new ValidationException(MessageFormat.format("{0} 不能为空", name));
        }
        return fieldValue;
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

    private <T extends Comparable<T>> int compareTo(T c1, T c2) {
        return c1.compareTo(c2);
    }

}
