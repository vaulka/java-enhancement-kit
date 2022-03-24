package com.pongsky.kit.excel.enums;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 解析类型
 *
 * @author pengsenhao
 **/
public enum ParseType {

    /**
     * 未知
     */
    UNKNOWN() {
        @Override
        public boolean validationFieldType(Field field) {
            return false;
        }

        @Override
        public boolean validationClassType(Object result) {
            return false;
        }
    },

    /**
     * String
     */
    STRING() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(String.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof String;
        }
    },

    /**
     * Enum
     */
    ENUM() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().getEnumConstants() != null;
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Enum;
        }
    },

    /**
     * Byte
     */
    BYTE() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Byte.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Byte;
        }
    },

    /**
     * Short
     */
    SHORT() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Short.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Short;
        }
    },

    /**
     * Integer
     */
    INTEGER() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Integer.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Integer;
        }
    },

    /**
     * Long
     */
    LONG() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Long.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Long;
        }
    },

    /**
     * Float
     */
    FLOAT() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Float.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Float;
        }
    },

    /**
     * Double
     */
    DOUBLE() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Double.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Double;
        }
    },

    /**
     * Character
     */
    CHARACTER() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Character.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Character;
        }
    },

    /**
     * Boolean
     */
    BOOLEAN() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Boolean.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Boolean;
        }
    },

    /**
     * List
     */
    LIST() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(List.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof List;
        }
    },

    /**
     * Set
     */
    SET() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Set.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Set;
        }
    },

    /**
     * Map
     */
    MAP() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Map.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Map;
        }
    },

    /**
     * BigInteger
     */
    BIG_INTEGER() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(BigInteger.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof BigInteger;
        }
    },

    /**
     * BigDecimal
     */
    BIG_DECIMAL() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(BigDecimal.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof BigDecimal;
        }
    },

    /**
     * Date
     */
    DATE() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Date.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Date;
        }
    },

    /**
     * LocalDate
     */
    LOCAL_DATE() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(LocalDate.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof LocalDate;
        }
    },

    /**
     * LocalTime
     */
    LOCAL_TIME() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(LocalTime.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof LocalTime;
        }
    },

    /**
     * LocalDateTime
     */
    LOCAL_DATE_TIME() {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(LocalDateTime.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof LocalDateTime;
        }
    },

    ;

    /**
     * 校验字段类型是否匹配
     *
     * @param field field
     * @return 校验字段类型是否匹配
     */
    public abstract boolean validationFieldType(Field field);

    /**
     * 校验类类型是否匹配
     *
     * @param result result
     * @return 校验类类型是否匹配
     */
    public abstract boolean validationClassType(Object result);

    /**
     * 所有 字段类型 列表
     */
    private static final List<ParseType> ALL = Arrays.stream(values())
            .collect(Collectors.toList());

    /**
     * 获取字段类型
     *
     * @param field field
     * @return 获取字段类型
     */
    public static ParseType getFieldType(Field field) {
        return ALL.stream()
                .filter(ft -> ft.validationFieldType(field))
                .findFirst()
                .orElse(ParseType.UNKNOWN);
    }

    /**
     * 获取类类型
     *
     * @param result result
     * @return 获取类类型
     */
    public static ParseType getClassType(Object result) {
        return ALL.stream()
                .filter(ft -> ft.validationClassType(result))
                .findFirst()
                .orElse(ParseType.UNKNOWN);
    }

}
