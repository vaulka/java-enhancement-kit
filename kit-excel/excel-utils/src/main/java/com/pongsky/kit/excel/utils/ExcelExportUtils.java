package com.pongsky.kit.excel.utils;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.annotation.Excels;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导出 excel 工具类
 *
 * @author pengsenhao
 **/
public class ExcelExportUtils {

    /**
     * 导出 excel 相关参数信息
     */
    private final ExcelExportInfo info;

    public ExcelExportUtils(Class<?> clazz) {
        info = new ExcelExportInfo();
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
    public SXSSFWorkbook export(List<?> results, String sheetName) {
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
     * 添加超链接
     *
     * @param cell          列
     * @param hyperLinkInfo 超链接信息
     */
    private void addHyperlink(Cell cell, String hyperLinkInfo) {
        CreationHelper createHelper = info.getWorkbook().getCreationHelper();
        Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.URL);
        hyperlink.setAddress(hyperLinkInfo);
        cell.setHyperlink(hyperlink);
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
        Comment comment = info.getDrawing().createCellComment(anchor);
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
            String columnName = StringUtils.isNotBlank(excel.value())
                    ? excel.value()
                    : StringUtils.isNotBlank(excel.name())
                    ? excel.name()
                    : field.getName();
            // 设置样式
            info.getCell().setCellStyle(this.getCellStyle(excel, true));
            // 填充内容
            info.getCell().setCellValue(new XSSFRichTextString(columnName));
            // 添加超链接
            if (StringUtils.isNotBlank(excel.hyperlink())) {
                this.addHyperlink(info.getCell(), excel.hyperlink());
            }
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
        for (Object result : info.getResults()) {
            info.setRow(info.getSheet().createRow(info.getRowNum()));
            for (int i = 0; i < info.getFields().size(); i++) {
                info.setCell(info.getRow().createCell(i));
                List<Object> fieldExcels = info.getFields().get(i);
                Field field = ExcelExportInfo.getField(fieldExcels);
                Excel excel = ExcelExportInfo.getExcel(fieldExcels);
                info.getCell().setCellStyle(this.getCellStyle(excel, false));
                Object fieldValue = ParseResultUtils.parseFieldValue(field, excel.attrs(), result);
                try {
                    excel.handler().getDeclaredConstructor().newInstance()
                            .exec(field, excel, fieldValue, info);
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

}
