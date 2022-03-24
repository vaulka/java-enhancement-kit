package com.pongsky.kit.excel.utils;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.annotation.Excels;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import com.pongsky.kit.excel.enums.FieldType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 导出 excel 工具类
 *
 * @author pengsenhao
 **/
public class ExcelExportUtils<T> {

    /**
     * 导出 excel 相关参数信息
     */
    private final ExcelExportInfo<T> info;

    public ExcelExportUtils(Class<T> clazz) {
        info = new ExcelExportInfo<>();
        List<Class<?>> classes = new ArrayList<>();
        this.getSuperclasses(classes, clazz);
        info.setFields(new ArrayList<>());
        for (Class<?> cla : classes) {
            List<Field> fieldList = Arrays.stream(cla.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Excel.class) || f.isAnnotationPresent(Excels.class))
                    .collect(Collectors.toList());
            for (Field field : fieldList) {
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null) {
                    info.getFields().add(List.of(field, excel));
                }
                Excels excels = field.getAnnotation(Excels.class);
                if (excels != null) {
                    for (Excel ex : excels.value()) {
                        info.getFields().add(List.of(field, ex));
                    }
                }
            }
        }
        info.getFields().sort(Comparator.comparing(f -> (ExcelExportInfo.getExcel(f).sort())));
        info.setWidths(new HashMap<>(info.getFields().size() * 2));
    }

    /**
     * 递归获取父 class 列表
     *
     * @param classes class 列表
     * @param clazz   class
     */
    private void getSuperclasses(List<Class<?>> classes, Class<?> clazz) {
        classes.add(clazz);
        if (!clazz.getSuperclass().getName().equals(Object.class.getName())) {
            this.getSuperclasses(classes, clazz.getSuperclass());
        }
    }

    /**
     * 导出 excel
     *
     * @param results   数据列表
     * @param sheetName 工作表名称
     * @return 导出 excel
     */
    public SXSSFWorkbook export(List<T> results, String sheetName) {
        info.setHeights(new HashMap<>(results.size() * 2));
        info.setResults(results);
        // 创建工作薄
        info.setWorkbook(new SXSSFWorkbook(results.size() + 1));
        info.getWorkbook().setCompressTempFiles(true);
        // 创建工作表
        info.setSheet(info.getWorkbook().createSheet(sheetName));
        // 创建绘图器
        info.setDrawing(info.getSheet().createDrawingPatriarch());
        // 构建 行名列表
        this.buildColumnTitles();
        // 构建 数据列表
        this.buildResults();
        // 宽度自适应
        for (int i = 0; i < info.getWidths().size(); i++) {
            info.getSheet().setColumnWidth(i, info.getWidths().get(i));
        }
        // 高度自适应
        for (Map.Entry<Integer, Short> entry : info.getHeights().entrySet()) {
            info.getSheet().getRow(entry.getKey()).setHeight(entry.getValue());
        }
        return info.getWorkbook();
    }

    /**
     * 获取单元格样式
     *
     * @param excel         导出 excel 相关信息
     * @param isColumnTitle 是否是列名
     * @return 获取单元格样式
     */
    private CellStyle getCellStyle(Excel excel, boolean isColumnTitle) {
        CellStyle cellStyle = info.getWorkbook().createCellStyle();
        cellStyle.setDataFormat(info.getWorkbook().createDataFormat().getFormat(excel.dataFormat()));
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(excel.alignment());
        cellStyle.setVerticalAlignment(excel.verticalAlignment());
        Font font = info.getWorkbook().createFont();
        font.setFontName(excel.fontName());
        font.setFontHeightInPoints(excel.fontSize());
        if (isColumnTitle) {
            // 列名 加粗
            font.setBold(true);
            // 列名 灰色 前景色
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 添加批注
     *
     * @param cell        列
     * @param commentInfo 批注信息
     */
    private void addComment(Cell cell, String commentInfo) {
        ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,
                cell.getColumnIndex(), cell.getRowIndex(),
                cell.getColumnIndex() + 2, cell.getRowIndex() + 2);
        SXSSFDrawing drawing = info.getSheet().createDrawingPatriarch();
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(new XSSFRichTextString(commentInfo));
        cell.setCellComment(comment);
    }

    /**
     * 构建 列名列表
     */
    private void buildColumnTitles() {
        info.setRow(info.getSheet().createRow(info.getRowNum()));
        for (int i = 0; i < info.getFields().size(); i++) {
            info.setCell(info.getRow().createCell(i));
            List<Object> fieldExcels = info.getFields().get(i);
            Field field = ExcelExportInfo.getField(fieldExcels);
            Excel excel = ExcelExportInfo.getExcel(fieldExcels);
            String columnName = StringUtils.isBlank(excel.name())
                    ? field.getName()
                    : excel.name();
            // 设置样式
            info.getCell().setCellStyle(this.getCellStyle(excel, true));
            // 填充内容
            info.getCell().setCellValue(new XSSFRichTextString(columnName));
            // 添加批注
            if (StringUtils.isNotBlank(excel.comment())) {
                this.addComment(info.getCell(), excel.comment());
            }
            info.setTextWidth(i, columnName.length());
        }
        info.rowNumPlusOne();
    }

    /**
     * 构建 数据列表
     */
    private void buildResults() {
        for (T result : info.getResults()) {
            info.setRow(info.getSheet().createRow(info.getRowNum()));
            for (int i = 0; i < info.getFields().size(); i++) {
                info.setCell(info.getRow().createCell(i));
                List<Object> fieldExcels = info.getFields().get(i);
                Field field = ExcelExportInfo.getField(fieldExcels);
                Excel excel = ExcelExportInfo.getExcel(fieldExcels);
                info.getCell().setCellStyle(this.getCellStyle(excel, false));
                Object fieldValue = this.parseFieldValue(field, excel, result);
                try {
                    excel.handler().getDeclaredConstructor().newInstance()
                            .exec(excel, fieldValue, info);
                } catch (InstantiationException
                        | IllegalAccessException
                        | InvocationTargetException
                        | NoSuchMethodException
                        | IOException e) {
                    throw new RuntimeException(e.getLocalizedMessage(), e);
                }
            }
            info.rowNumPlusOne();
        }
    }

    /**
     * 解析数据
     *
     * @param field  field
     * @param excel  导出 excel 相关信息
     * @param result 行数据
     * @return 解析数据
     */
    private Object parseFieldValue(Field field, Excel excel, Object result) {
        Object fieldValue = this.getFieldValue(result, field);
        if (StringUtils.isNotBlank(excel.attrs())) {
            fieldValue = this.parseFieldValueByAttrs(excel.attrs(), fieldValue);
        }
        return fieldValue;
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
    private Object parseFieldValueByAttrs(String attrs, Object value) {
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
            FieldType type = FieldType.getType(field);
            val = this.getFieldValue(value, field);
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
                return this.parseFieldValueByAttrs(lastAttr, val);
            }
            return val;
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(MessageFormat.format(
                    "attr {0} 解析失败：{1}",
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
    private Object getFieldValue(Object obj, Field field) {
        field.setAccessible(true);
        Object result;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(MessageFormat.format(
                    "获取 {0}.{1} 属性值失败：{2}",
                    obj.getClass().getName(), field.getName(), e.getLocalizedMessage()),
                    e);
        }
        return result;
    }

}
