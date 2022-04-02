package com.pongsky.kit.excel.enums;

import com.pongsky.kit.excel.handler.export.ExcelBigDecimalExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelBooleanExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelDateExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelDayOfWeekExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelLocalDateExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelLocalDateTimeExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelLocalTimeExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelMonthExportHandler;
import com.pongsky.kit.excel.handler.export.ExcelStringExportHandler;
import com.pongsky.kit.excel.handler.read.ExcelBigDecimalImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelBigIntegerImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelBooleanImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelByteImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelCharacterImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelDateImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelDayOfWeekImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelDoubleImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelFloatImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelIntegerImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelLocalDateImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelLocalDateTimeImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelLocalTimeImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelLongImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelMonthImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelShortImportHandler;
import com.pongsky.kit.excel.handler.read.ExcelStringImportHandler;
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
    UNKNOWN(1, null, null) {
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
    ENUM(1, ExcelStringExportHandler.class, null) {
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
    STRING(0, ExcelStringExportHandler.class, ExcelStringImportHandler.class) {
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
    BYTE(0, ExcelStringExportHandler.class, ExcelByteImportHandler.class) {
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
    SHORT(0, ExcelStringExportHandler.class, ExcelShortImportHandler.class) {
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
    INTEGER(0, ExcelStringExportHandler.class, ExcelIntegerImportHandler.class) {
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
    LONG(0, ExcelStringExportHandler.class, ExcelLongImportHandler.class) {
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
    FLOAT(0, ExcelStringExportHandler.class, ExcelFloatImportHandler.class) {
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
    DOUBLE(0, ExcelStringExportHandler.class, ExcelDoubleImportHandler.class) {
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
    CHARACTER(0, ExcelStringExportHandler.class, ExcelCharacterImportHandler.class) {
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
    BOOLEAN(0, ExcelBooleanExportHandler.class, ExcelBooleanImportHandler.class) {
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
    LIST(0, ExcelStringExportHandler.class, null) {
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
    SET(0, ExcelStringExportHandler.class, null) {
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
    MAP(0, ExcelStringExportHandler.class, null) {
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
    BIG_INTEGER(0, ExcelStringExportHandler.class, ExcelBigIntegerImportHandler.class) {
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
    BIG_DECIMAL(0, ExcelBigDecimalExportHandler.class, ExcelBigDecimalImportHandler.class) {
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
    DATE(0, ExcelDateExportHandler.class, ExcelDateImportHandler.class) {
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
    LOCAL_DATE(0, ExcelLocalDateExportHandler.class, ExcelLocalDateImportHandler.class) {
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
    LOCAL_TIME(0, ExcelLocalTimeExportHandler.class, ExcelLocalTimeImportHandler.class) {
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
    LOCAL_DATE_TIME(0, ExcelLocalDateTimeExportHandler.class, ExcelLocalDateTimeImportHandler.class) {
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
    DAY_OF_WEEK(0, ExcelDayOfWeekExportHandler.class, ExcelDayOfWeekImportHandler.class) {
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
    MONTH(0, ExcelMonthExportHandler.class, ExcelMonthImportHandler.class) {
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
     * 导出 列值处理器
     */
    private final Class<? extends ExcelExportHandler> exportHandler;

    /**
     * 导入 列值处理器
     */
    private final Class<? extends ExcelImportHandler> importHandler;

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
