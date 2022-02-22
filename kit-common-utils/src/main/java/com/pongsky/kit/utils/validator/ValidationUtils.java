package com.pongsky.kit.utils.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 校验工具类
 *
 * @author pengsenhao
 */
public class ValidationUtils {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 对数据进行校验
     *
     * @param data 数据
     * @param <T>  范型
     * @return 错误信息
     */
    public static <T> String convertErrorMsg(T data) {
        return convertErrorMsg(data, Default.class);
    }

    /**
     * 对数据进行校验
     *
     * @param data           数据
     * @param validatorGroup 校验组
     * @param <T>            范型
     * @return 错误信息
     */
    public static <T> String convertErrorMsg(T data, Class<?> validatorGroup) {
        Map<String, StringBuffer> errorMap = new HashMap<>(16);
        Set<ConstraintViolation<T>> validate = VALIDATOR.validate(data, validatorGroup);
        if (validate != null && validate.size() > 0) {
            String property;
            for (ConstraintViolation<T> cv : validate) {
                // 这里循环获取错误信息，可以自定义格式
                property = cv.getPropertyPath().toString();
                if (errorMap.get(property) != null) {
                    errorMap.get(property).append(",").append(cv.getMessage());
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            errorMap.forEach((k, v) -> stringBuilder.append(k).append(" ").append(v.toString()).append("；"));
            return stringBuilder.substring(0, stringBuilder.toString().length() - 1);
        } else {
            return null;
        }
    }

}
