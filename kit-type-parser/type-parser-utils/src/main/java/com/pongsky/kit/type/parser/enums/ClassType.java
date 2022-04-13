package com.pongsky.kit.type.parser.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
 * 类类型
 *
 * @author pengsenhao
 **/
@Getter
@AllArgsConstructor
public enum ClassType {

    /**
     * String
     */
    STRING("String", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof String;
        }
    },

    /**
     * Byte
     */
    BYTE("Byte", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Byte;
        }
    },

    /**
     * Short
     */
    SHORT("Short", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Short;
        }
    },

    /**
     * Integer
     */
    INTEGER("Integer", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Integer;
        }
    },

    /**
     * Long
     */
    LONG("Long", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Long;
        }
    },

    /**
     * Float
     */
    FLOAT("Float", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Float;
        }
    },

    /**
     * Double
     */
    DOUBLE("Double", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Double;
        }
    },

    /**
     * Character
     */
    CHARACTER("Character", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Character;
        }
    },

    /**
     * Boolean
     */
    BOOLEAN("Boolean", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Boolean;
        }
    },

    /**
     * Array
     */
    ARRAY("Array", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Object[];
        }
    },

    /**
     * List
     */
    LIST("List", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof List;
        }
    },

    /**
     * Set
     */
    SET("Set", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Set;
        }
    },

    /**
     * Map
     */
    MAP("Map", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Map;
        }
    },

    /**
     * BigInteger
     */
    BIG_INTEGER("BigInteger", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof BigInteger;
        }
    },

    /**
     * BigDecimal
     */
    BIG_DECIMAL("BigDecimal", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof BigDecimal;
        }
    },

    /**
     * Date
     */
    DATE("Date", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Date;
        }
    },

    /**
     * LocalDate
     */
    LOCAL_DATE("LocalDate", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof LocalDate;
        }
    },

    /**
     * LocalTime
     */
    LOCAL_TIME("LocalTime", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof LocalTime;
        }
    },

    /**
     * LocalDateTime
     */
    LOCAL_DATE_TIME("LocalDateTime", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof LocalDateTime;
        }
    },

    /**
     * DayOfWeek
     */
    DAY_OF_WEEK("DayOfWeek", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof DayOfWeek;
        }
    },

    /**
     * Month
     */
    MONTH("Month", 0) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Month;
        }
    },

    /**
     * Enum
     */
    ENUM("Enum", Integer.MAX_VALUE) {
        @Override
        public boolean parser(Object obj) {
            return obj instanceof Enum;
        }
    },

    /**
     * Null
     */
    NULL("Null", Integer.MAX_VALUE) {
        @Override
        public boolean parser(Object obj) {
            return obj == null;
        }
    },

    /**
     * Object
     * <p>
     * 兜底类型
     */
    OBJECT("Object", Integer.MAX_VALUE) {
        @Override
        public boolean parser(Object obj) {
            return false;
        }
    },

    ;

    /**
     * 类类型
     */
    private final String type;

    /**
     * 类解析顺序
     * <p>
     * 有些 Java 内置常用枚举优先解析成特定类型
     */
    private final Integer sort;

    /**
     * 校验类类型是否匹配
     *
     * @param obj obj
     * @return 校验类类型是否匹配
     */
    public abstract boolean parser(Object obj);

    /**
     * 所有 类类型 列表
     */
    private static final List<ClassType> ALL = Arrays.stream(values())
            .sorted(Comparator.comparing(ct -> ct.sort))
            .collect(Collectors.toList());

    /**
     * 获取类类型
     *
     * @param obj 类
     * @return 获取类类型
     */
    public static ClassType getType(Object obj) {
        return ALL.stream()
                .filter(ct -> ct.parser(obj))
                .findFirst()
                .orElse(ClassType.OBJECT);
    }

}
