package com.pongsky.kit.excel.utils;

import com.pongsky.kit.excel.enums.ParseType;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 多层数据解析工具类
 *
 * @author pengsenhao
 **/
public class ParseResultUtils {

    /**
     * 解析数据
     *
     * @param field  field
     * @param attrs  多层级属性名称
     * @param result 行数据
     * @return 解析数据
     */
    public static Object parseFieldValue(Field field, String attrs, Object result) {
        Object fieldValue = ParseResultUtils.getFieldValue(result, field);
        return ParseResultUtils.parseFieldValue(attrs, fieldValue);
    }

    /**
     * 解析数据
     *
     * @param attrs  多层级属性名称
     * @param result 行数据
     * @return 解析数据
     */
    public static Object parseFieldValue(String attrs, Object result) {
        if (StringUtils.isNotBlank(attrs)) {
            result = ParseResultUtils.parseFieldValueByAttrs(attrs, result);
        }
        return result;
    }

    /**
     * 点符号
     */
    private static final String POINT = ".";

    /**
     * 左方括号
     */
    private static final String LEFT_SQUARE_BRACKETS = "[";

    /**
     * 右方括号
     */
    private static final String RIGHT_SQUARE_BRACKETS = "]";

    /**
     * 解析 attrs 层级数据
     *
     * @param attrs attrs
     * @param value 基层数据
     * @return 解析 attrs 层级数据
     */
    private static Object parseFieldValueByAttrs(String attrs, Object value) {
        String attr = attrs;
        String lastAttr = null;
        if (attr.contains(POINT)) {
            attr = attr.substring(0, attr.indexOf(POINT));
            lastAttr = attrs.substring(attr.length());
            if (StringUtils.isNotBlank(lastAttr) && lastAttr.startsWith(POINT)) {
                lastAttr = lastAttr.substring(1);
            }
        }
        Object index = null;
        if (attr.contains(LEFT_SQUARE_BRACKETS) && attr.contains(RIGHT_SQUARE_BRACKETS)) {
            index = attr.substring(attr.indexOf(LEFT_SQUARE_BRACKETS) + 1, attr.indexOf(RIGHT_SQUARE_BRACKETS));
            attr = attr.substring(0, attr.lastIndexOf(LEFT_SQUARE_BRACKETS));
        }
        Object val;
        try {
            Field field = value.getClass().getDeclaredField(attr);
            ParseType type = ParseType.getFieldType(field);
            val = ParseResultUtils.getFieldValue(value, field);
            if (index != null) {
                switch (type) {
                    case MAP:
                        val = ((Map<?, ?>) val).get(index);
                        break;
                    case LIST:
                        val = ((List<?>) val).get(Integer.parseInt(index.toString()));
                        break;
                    case SET:
                        val = ((Set<?>) val).toArray()[Integer.parseInt(index.toString())];
                        break;
                    default:
                        break;
                }
            }
            if (StringUtils.isNotBlank(lastAttr)) {
                return ParseResultUtils.parseFieldValueByAttrs(lastAttr, val);
            }
            return val;
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format(
                    "attr {0} 属性解析失败：{1}",
                    attr, e.getLocalizedMessage()),
                    e);
        }
    }

    /**
     * 获取属性值
     *
     * @param obj   obj
     * @param field field
     * @return 获取属性值
     */
    private static Object getFieldValue(Object obj, Field field) {
        field.setAccessible(true);
        Object result;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(MessageFormat.format(
                    "{0}.{1} 属性值获取失败：{2}",
                    obj.getClass().getName(), field.getName(), e.getLocalizedMessage()),
                    e);
        }
        return result;
    }

}
