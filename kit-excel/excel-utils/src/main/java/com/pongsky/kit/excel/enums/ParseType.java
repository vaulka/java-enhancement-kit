package com.pongsky.kit.excel.enums;

import com.pongsky.kit.excel.handler.ExcelBigDecimalHandler;
import com.pongsky.kit.excel.handler.ExcelBooleanHandler;
import com.pongsky.kit.excel.handler.ExcelDateHandler;
import com.pongsky.kit.excel.handler.ExcelDayOfWeekHandler;
import com.pongsky.kit.excel.handler.ExcelHandler;
import com.pongsky.kit.excel.handler.ExcelLocalDateHandler;
import com.pongsky.kit.excel.handler.ExcelLocalDateTimeHandler;
import com.pongsky.kit.excel.handler.ExcelLocalTimeHandler;
import com.pongsky.kit.excel.handler.ExcelMonthHandler;
import com.pongsky.kit.excel.handler.ExcelStringHandler;
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
 * 解析类型
 *
 * @author pengsenhao
 **/
@Getter
@AllArgsConstructor
public enum ParseType {

    /**
     * 未知
     * <p>
     * 兜底类型
     */
    UNKNOWN(1, ExcelStringHandler.class) {
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
     * Enum
     */
    ENUM(1, ExcelStringHandler.class) {
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
     * String
     */
    STRING(0, ExcelStringHandler.class) {
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
     * Byte
     */
    BYTE(0, ExcelStringHandler.class) {
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
    SHORT(0, ExcelStringHandler.class) {
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
    INTEGER(0, ExcelStringHandler.class) {
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
    LONG(0, ExcelStringHandler.class) {
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
    FLOAT(0, ExcelStringHandler.class) {
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
    DOUBLE(0, ExcelStringHandler.class) {
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
    CHARACTER(0, ExcelStringHandler.class) {
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
    BOOLEAN(0, ExcelBooleanHandler.class) {
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
    LIST(0, ExcelStringHandler.class) {
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
    SET(0, ExcelStringHandler.class) {
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
    MAP(0, ExcelStringHandler.class) {
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
    BIG_INTEGER(0, ExcelStringHandler.class) {
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
    BIG_DECIMAL(0, ExcelBigDecimalHandler.class) {
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
    DATE(0, ExcelDateHandler.class) {
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
    LOCAL_DATE(0, ExcelLocalDateHandler.class) {
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
    LOCAL_TIME(0, ExcelLocalTimeHandler.class) {
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
    LOCAL_DATE_TIME(0, ExcelLocalDateTimeHandler.class) {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(LocalDateTime.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof LocalDateTime;
        }
    },

    /**
     * DayOfWeek
     */
    DAY_OF_WEEK(0, ExcelDayOfWeekHandler.class) {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(DayOfWeek.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof DayOfWeek;
        }
    },

    /**
     * MONTH
     */
    MONTH(0, ExcelMonthHandler.class) {
        @Override
        public boolean validationFieldType(Field field) {
            return field.getType().toString().equals(Month.class.toString());
        }

        @Override
        public boolean validationClassType(Object result) {
            return result instanceof Month;
        }
    },

    ;

    /**
     * 解析顺序
     * <p>
     * 有些 Java 内置常用枚举优先解析成特定处理器，不匹配则走枚举统一处理器
     */
    private final Integer sort;

    /**
     * 列值处理器
     */
    private final Class<? extends ExcelHandler> handler;

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
            .sorted(Comparator.comparing(p -> p.sort))
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
