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
import com.pongsky.kit.type.parser.enums.ClassType;
import com.pongsky.kit.type.parser.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
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
     * String
     */
    STRING(ClassType.STRING, FieldType.STRING, ExcelStringExportHandler.class, ExcelStringImportHandler.class),

    /**
     * Byte
     */
    BYTE(ClassType.BYTE, FieldType.BYTE, ExcelStringExportHandler.class, ExcelByteImportHandler.class),

    /**
     * Short
     */
    SHORT(ClassType.SHORT, FieldType.SHORT, ExcelStringExportHandler.class, ExcelShortImportHandler.class),

    /**
     * Integer
     */
    INTEGER(ClassType.INTEGER, FieldType.INTEGER, ExcelStringExportHandler.class, ExcelIntegerImportHandler.class),

    /**
     * Long
     */
    LONG(ClassType.LONG, FieldType.LONG, ExcelStringExportHandler.class, ExcelLongImportHandler.class),

    /**
     * Float
     */
    FLOAT(ClassType.FLOAT, FieldType.FLOAT, ExcelStringExportHandler.class, ExcelFloatImportHandler.class),

    /**
     * Double
     */
    DOUBLE(ClassType.DOUBLE, FieldType.DOUBLE, ExcelStringExportHandler.class, ExcelDoubleImportHandler.class),

    /**
     * Character
     */
    CHARACTER(ClassType.CHARACTER, FieldType.CHARACTER, ExcelStringExportHandler.class, ExcelCharacterImportHandler.class),

    /**
     * Boolean
     */
    BOOLEAN(ClassType.BOOLEAN, FieldType.BOOLEAN, ExcelBooleanExportHandler.class, ExcelBooleanImportHandler.class),

    /**
     * List
     */
    LIST(ClassType.LIST, FieldType.LIST, ExcelStringExportHandler.class, null),

    /**
     * Set
     */
    SET(ClassType.SET, FieldType.SET, ExcelStringExportHandler.class, null),

    /**
     * Map
     */
    MAP(ClassType.MAP, FieldType.MAP, ExcelStringExportHandler.class, null),

    /**
     * BigInteger
     */
    BIG_INTEGER(ClassType.BIG_INTEGER, FieldType.BIG_INTEGER, ExcelStringExportHandler.class, ExcelBigIntegerImportHandler.class),

    /**
     * BigDecimal
     */
    BIG_DECIMAL(ClassType.BIG_DECIMAL, FieldType.BIG_DECIMAL, ExcelBigDecimalExportHandler.class, ExcelBigDecimalImportHandler.class),

    /**
     * Date
     */
    DATE(ClassType.DATE, FieldType.DATE, ExcelDateExportHandler.class, ExcelDateImportHandler.class),

    /**
     * LocalDate
     */
    LOCAL_DATE(ClassType.LOCAL_DATE, FieldType.LOCAL_DATE, ExcelLocalDateExportHandler.class, ExcelLocalDateImportHandler.class),

    /**
     * LocalTime
     */
    LOCAL_TIME(ClassType.LOCAL_TIME, FieldType.LOCAL_TIME, ExcelLocalTimeExportHandler.class, ExcelLocalTimeImportHandler.class),

    /**
     * LocalDateTime
     */
    LOCAL_DATE_TIME(ClassType.LOCAL_DATE_TIME, FieldType.LOCAL_DATE_TIME, ExcelLocalDateTimeExportHandler.class, ExcelLocalDateTimeImportHandler.class),

    /**
     * DayOfWeek
     */
    DAY_OF_WEEK(ClassType.DAY_OF_WEEK, FieldType.DAY_OF_WEEK, ExcelDayOfWeekExportHandler.class, ExcelDayOfWeekImportHandler.class),

    /**
     * MONTH
     */
    MONTH(ClassType.MONTH, FieldType.MONTH, ExcelMonthExportHandler.class, ExcelMonthImportHandler.class),

    /**
     * Enum
     */
    ENUM(ClassType.ENUM, FieldType.ENUM, ExcelStringExportHandler.class, null),

    /**
     * OBJECT
     * <p>
     * 兜底类型
     */
    OBJECT(ClassType.OBJECT, FieldType.OBJECT, null, null),

    ;

    /**
     * 类类型
     */
    private final ClassType classType;

    /**
     * 字段类型
     */
    private final FieldType fieldType;

    /**
     * 导出 列值处理器
     */
    private final Class<? extends ExcelExportHandler> exportHandler;

    /**
     * 导入 列值处理器
     */
    private final Class<? extends ExcelImportHandler> importHandler;

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
        FieldType fieldType = FieldType.getType(field);
        return ALL.stream()
                .filter(pt -> pt.fieldType == fieldType)
                .findFirst()
                .orElse(ParseType.OBJECT);
    }

    /**
     * 获取类类型
     *
     * @param obj obj
     * @return 获取类类型
     */
    public static ParseType getClassType(Object obj) {
        ClassType classType = ClassType.getType(obj);
        return ALL.stream()
                .filter(pt -> pt.classType == classType)
                .findFirst()
                .orElse(ParseType.OBJECT);
    }

}
