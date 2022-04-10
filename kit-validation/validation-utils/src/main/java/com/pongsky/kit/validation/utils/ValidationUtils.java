package com.pongsky.kit.validation.utils;

import com.pongsky.kit.type.parser.utils.CollectionParserUtils;
import com.pongsky.kit.type.parser.utils.FieldParserUtils;
import com.pongsky.kit.validation.annotation.PropertyName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validation 工具类
 *
 * @author pengsenhao
 **/
public class ValidationUtils {

    /**
     * 参数校验错误总数信息
     */
    private static final String ERROR_COUNT_MSG = "参数校验失败，一共有 {0} 处错误，详情如下：";

    /**
     * 获取校验异常信息
     *
     * @param bindingResult bindingResult
     * @return 获取校验异常信息
     */
    public static String getErrorMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(ERROR_COUNT_MSG, bindingResult.getErrorCount()));
        if (bindingResult.getTarget() == null) {
            bindingResult.getFieldErrors().forEach(error ->
                    ValidationUtils.buildErrorMessage(sb, error.getField(), error.getDefaultMessage()));
            return sb.toString();
        }
        for (FieldError error : bindingResult.getFieldErrors()) {
            List<Field> fields = new ArrayList<>();
            Map<Field, String> indexMap = new HashMap<>(16);
            ValidationUtils.getField(fields, indexMap, bindingResult.getTarget().getClass(), error.getField());
            String field = fields.stream()
                    .map(f -> {
                        PropertyName propertyName = f.getAnnotation(PropertyName.class);
                        String fieldName = propertyName != null && StringUtils.isNotBlank(propertyName.value())
                                ? propertyName.value()
                                : f.getName();
                        String index = indexMap.get(f);
                        if (index != null) {
                            fieldName += index;
                        }
                        return fieldName;
                    }).collect(Collectors.joining(POINT));
            ValidationUtils.buildErrorMessage(sb, field, error.getDefaultMessage());
        }
        return sb.toString();
    }

    /**
     * 构建错误信息详情
     *
     * @param sb           sb
     * @param fieldName    字段名称
     * @param errorMessage 错误信息
     */
    private static void buildErrorMessage(StringBuilder sb, String fieldName, String errorMessage) {
        sb.append(" ").append(fieldName).append(" ").append(errorMessage).append(";");
    }

    /**
     * 点符号
     */
    private static final String POINT = ".";

    /**
     * 左大括号
     */
    private static final String LEFT_BRACE = "[";

    /**
     * 右大括号
     */
    private static final String RIGHT_BRACE = "]";

    /**
     * 获取多层级属性
     *
     * @param fields    字段列表
     * @param indexMap  字段下标列表
     * @param clazz     待解析的类
     * @param fieldName 字段名称
     */
    private static void getField(List<Field> fields,
                                 Map<Field, String> indexMap,
                                 Class<?> clazz,
                                 String fieldName) {
        List<Field> rootFields = FieldParserUtils.getSuperFields(clazz);
        String baseFieldName = fieldName.contains(POINT)
                ? fieldName.substring(0, fieldName.indexOf(POINT))
                : "",
                subFieldName = fieldName.contains(POINT)
                        ? fieldName.substring(baseFieldName.length() + 1)
                        : "";
        if (fieldName.contains(POINT)) {
            if (baseFieldName.contains(LEFT_BRACE) && baseFieldName.contains(RIGHT_BRACE)) {
                // 示例：user.name.tags[x]
                // 示例：user.name.customer[x].name
                ValidationUtils.getFieldByCollections(rootFields, fields, indexMap, baseFieldName, subFieldName);
            } else {
                // 示例：user.name
                rootFields.stream()
                        .filter(f -> f.getName().equals(baseFieldName))
                        .findFirst().ifPresent(f -> {
                            fields.add(f);
                            ValidationUtils.getField(fields, indexMap, f.getType(), subFieldName);
                        });
            }
        } else if (fieldName.contains(LEFT_BRACE) && fieldName.contains(RIGHT_BRACE)) {
            // 示例：tags[x]
            ValidationUtils.getFieldByCollections(rootFields, fields, indexMap, fieldName, subFieldName);
        } else {
            // 示例：name
            rootFields.stream()
                    .filter(f -> f.getName().equals(fieldName))
                    .findFirst()
                    .ifPresent(fields::add);
        }
    }

    /**
     * 获取集合的字段信息
     *
     * @param rootFields    解析类的所有字段列表
     * @param fields        当前字段解析成功的字段列表
     * @param indexMap      当前字段的下标信息 MAP
     * @param baseFieldName 基类的字段名称
     * @param subFieldName  子类的字段名称
     */
    private static void getFieldByCollections(List<Field> rootFields,
                                              List<Field> fields,
                                              Map<Field, String> indexMap,
                                              String baseFieldName,
                                              String subFieldName) {
        // 示例：user.name.tags[x]
        // 示例：user.name.customer[x].name
        String arrayFieldName = baseFieldName.substring(0, baseFieldName.indexOf(LEFT_BRACE));
        String index = baseFieldName.substring(baseFieldName.indexOf(LEFT_BRACE));
        rootFields.stream()
                .filter(f -> f.getName().equals(arrayFieldName))
                .findFirst()
                .ifPresent(f -> {
                    fields.add(f);
                    indexMap.put(f, index);
                    if (subFieldName.length() > 0) {
                        Class<?> elementType = CollectionParserUtils.getElementType(f);
                        ValidationUtils.getField(fields, indexMap, elementType, subFieldName);
                    }
                });
    }

}
