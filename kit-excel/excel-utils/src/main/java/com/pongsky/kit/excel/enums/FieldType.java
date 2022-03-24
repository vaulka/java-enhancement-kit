package com.pongsky.kit.excel.enums;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字段类型
 *
 * @author pengsenhao
 **/
public enum FieldType {

    /**
     * 未知
     */
    UNKNOWN() {
        @Override
        public boolean validationType(Field field) {
            return false;
        }
    },

    /**
     * String
     */
    STRING() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(String.class.toString());
        }
    },

    /**
     * Enum
     */
    ENUM() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().getEnumConstants() != null;
        }
    },

    /**
     * Byte
     */
    BYTE() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Byte.class.toString());
        }
    },

    /**
     * Short
     */
    SHORT() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Short.class.toString());
        }
    },

    /**
     * Integer
     */
    INTEGER() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Integer.class.toString());
        }
    },

    /**
     * Long
     */
    LONG() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Long.class.toString());
        }
    },

    /**
     * Float
     */
    FLOAT() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Float.class.toString());
        }
    },

    /**
     * Double
     */
    DOUBLE() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Double.class.toString());
        }
    },

    /**
     * Character
     */
    CHARACTER() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Character.class.toString());
        }
    },

    /**
     * Boolean
     */
    BOOLEAN() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Boolean.class.toString());
        }
    },

    /**
     * List
     */
    LIST() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(List.class.toString());
        }
    },

    /**
     * Set
     */
    SET() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Set.class.toString());
        }
    },

    /**
     * Map
     */
    MAP() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Map.class.toString());
        }
    },

    /**
     * BigInteger
     */
    BIG_INTEGER() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(BigInteger.class.toString());
        }
    },

    /**
     * BigDecimal
     */
    BIG_DECIMAL() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(BigDecimal.class.toString());
        }
    },

    /**
     * Date
     */
    DATE() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(Date.class.toString());
        }
    },

    /**
     * LocalDate
     */
    LOCAL_DATE() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(java.time.LocalDate.class.toString());
        }
    },

    /**
     * LocalTime
     */
    LOCAL_TIME() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(LocalTime.class.toString());
        }
    },

    /**
     * LocalDateTime
     */
    LOCAL_DATE_TIME() {
        @Override
        public boolean validationType(Field field) {
            return field.getType().toString().equals(LocalDateTime.class.toString());
        }
    },

    ;

    /**
     * 校验字段类型是否匹配
     *
     * @param field field
     * @return 校验字段类型是否匹配
     */
    public abstract boolean validationType(Field field);

    /**
     * 所有 字段类型 列表
     */
    private static final List<FieldType> ALL = Arrays.stream(values())
            .collect(Collectors.toList());

    /**
     * 获取字段类型
     *
     * @param field field
     * @return 获取字段类型
     */
    public static FieldType getType(Field field) {
        return ALL.stream()
                .filter(ft -> ft.validationType(field))
                .findFirst()
                .orElse(FieldType.UNKNOWN);
    }

}
