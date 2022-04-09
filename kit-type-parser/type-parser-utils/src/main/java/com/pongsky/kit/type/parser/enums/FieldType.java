package com.pongsky.kit.type.parser.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 解析字段类型
 *
 * @author pengsenhao
 **/
@Getter
@AllArgsConstructor
public enum FieldType {

    /**
     * String
     */
    STRING(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == String.class;
        }
    },

    /**
     * Byte
     */
    BYTE(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Byte.class;
        }
    },

    /**
     * Short
     */
    SHORT(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Short.class;
        }
    },

    /**
     * Integer
     */
    INTEGER(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Integer.class;
        }
    },

    /**
     * Long
     */
    LONG(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Long.class;
        }
    },

    /**
     * Float
     */
    FLOAT(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Float.class;
        }
    },

    /**
     * Double
     */
    DOUBLE(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Double.class;
        }
    },

    /**
     * Character
     */
    CHARACTER(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Character.class;
        }
    },

    /**
     * Boolean
     */
    BOOLEAN(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Boolean.class;
        }
    },

    /**
     * Array
     */
    ARRAY(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType().isArray();
        }
    },

    /**
     * List
     */
    LIST(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == List.class;
        }
    },

    /**
     * Set
     */
    SET(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Set.class;
        }
    },

    /**
     * Map
     */
    MAP(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Map.class;
        }
    },

    /**
     * BigInteger
     */
    BIG_INTEGER(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == BigInteger.class;
        }
    },

    /**
     * BigDecimal
     */
    BIG_DECIMAL(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == BigDecimal.class;
        }
    },

    /**
     * Date
     */
    DATE(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Date.class;
        }
    },

    /**
     * LocalDate
     */
    LOCAL_DATE(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == LocalDate.class;
        }
    },

    /**
     * LocalTime
     */
    LOCAL_TIME(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == LocalTime.class;
        }
    },

    /**
     * LocalDateTime
     */
    LOCAL_DATE_TIME(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == LocalDateTime.class;
        }
    },

    /**
     * DayOfWeek
     */
    DAY_OF_WEEK(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == DayOfWeek.class;
        }
    },

    /**
     * MONTH
     */
    MONTH(0) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType() == Month.class;
        }
    },

    /**
     * Enum
     */
    ENUM(Integer.MAX_VALUE) {
        @Override
        public boolean parser(Field field) {
            if (field == null) {
                return false;
            }
            return field.getType().isEnum();
        }
    },

    /**
     * 未知
     * <p>
     * 兜底类型
     */
    UNKNOWN(Integer.MAX_VALUE) {
        @Override
        public boolean parser(Field field) {
            return false;
        }
    },

    ;

    /**
     * 字段解析顺序
     * <p>
     * 有些 Java 内置常用枚举优先解析成特定类型
     */
    private final Integer sort;

    /**
     * 校验字段类型是否匹配
     *
     * @param field 字段
     * @return 校验字段类型是否匹配
     */
    public abstract boolean parser(Field field);

    /**
     * 所有 字段类型 列表
     */
    private static final List<FieldType> ALL = Arrays.stream(values())
            .sorted(Comparator.comparing(ft -> ft.sort))
            .collect(Collectors.toList());

    /**
     * 获取字段类型
     *
     * @param field 字段
     * @return 获取字段类型
     */
    public static FieldType getType(Field field) {
        return ALL.stream()
                .filter(ft -> ft.parser(field))
                .findFirst()
                .orElse(FieldType.UNKNOWN);
    }

}
