package com.pongsky.kit.excel.utils;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.annotation.ExcelPropertys;
import com.pongsky.kit.excel.entity.ExcelImportInfo;
import com.pongsky.kit.excel.enums.ExcelType;
import com.pongsky.kit.excel.enums.ParseType;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class ExcelImportUtils<T> {

    /**
     * 导入 excel 相关参数信息
     */
    private final ExcelImportInfo<T> info;

    public ExcelImportUtils(Class<T> clazz) {
        info = new ExcelImportInfo<>(clazz);
        List<Class<?>> classes = new ArrayList<>();
        this.getSuperclasses(classes, clazz);
        info.setFields(new ArrayList<>());
        int titleMaxNum = 1;
        for (Class<?> cla : classes) {
            List<Field> fieldList = Arrays.stream(cla.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(ExcelProperty.class) || f.isAnnotationPresent(ExcelPropertys.class))
                    .collect(Collectors.toList());
            for (Field field : fieldList) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                if (excelProperty != null) {
                    titleMaxNum = Integer.max(titleMaxNum, excelProperty.yHead().length);
                    info.getFields().add(Arrays.asList(field, excelProperty));
                }
                ExcelPropertys excelPropertys = field.getAnnotation(ExcelPropertys.class);
                if (excelPropertys != null) {
                    for (ExcelProperty ex : excelPropertys.value()) {
                        titleMaxNum = Integer.max(titleMaxNum, ex.yHead().length);
                        info.getFields().add(Arrays.asList(field, ex));
                    }
                }
            }
        }
        info.getFields().sort(Comparator.comparing(f -> (ExcelImportInfo.getExcel(f).sort())));
        info.setTitleMaxNum(titleMaxNum);
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
     * 空列值
     */
    private static final short NULL_CELL_NUM = -1;

    /**
     * 读取 excel 文件
     *
     * @param filename    文件名称
     * @param inputStream inputStream
     * @param sheetName   工作表名称
     * @return 读取数据结果列表
     * @throws IOException                  IOException
     * @throws ReflectiveOperationException ReflectiveOperationException
     */
    public List<T> read(String filename, InputStream inputStream, String sheetName) throws IOException, ReflectiveOperationException {
        if (info.getFields().size() == 0) {
            // 没有任何字段需要写入则直接返回空结果
            return Collections.emptyList();
        }
        // 获取工作薄
        try (Workbook workbook = this.getWorkbook(filename, inputStream)) {
            return this.read(workbook, sheetName);
        }
    }

    /**
     * 读取 excel 文件
     *
     * @param file      excel 文件
     * @param sheetName 工作表名称
     * @return 读取数据结果列表
     * @throws IOException                  IOException
     * @throws ReflectiveOperationException ReflectiveOperationException
     */
    public List<T> read(File file, String sheetName) throws IOException, ReflectiveOperationException {
        if (info.getFields().size() == 0) {
            // 没有任何字段需要写入则直接返回空结果
            return Collections.emptyList();
        }
        // 获取工作薄
        try (Workbook workbook = this.getWorkbook(file)) {
            return this.read(workbook, sheetName);
        }
    }

    /**
     * 读取 excel 文件
     *
     * @param workbook  工作薄
     * @param sheetName 工作表名称
     * @return 读取数据结果列表
     * @throws ReflectiveOperationException ReflectiveOperationException
     */
    public List<T> read(Workbook workbook, String sheetName) throws ReflectiveOperationException {
        if (workbook == null) {
            return Collections.emptyList();
        }
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            // 获取不到工作表则直接返回空结果
            return Collections.emptyList();
        }
        Map<String, PictureData> pictureDates;
        switch (excelType) {
            case XLS:
                pictureDates = this.getPictureDataByXls((HSSFWorkbook) workbook, (HSSFSheet) sheet);
                break;
            case XLSX:
            default:
                pictureDates = this.getPictureDataByXlsx((XSSFSheet) sheet);
                break;
        }
        List<T> results = new ArrayList<>();
        // 循环读取行数据
        for (int rowNum = info.getTitleMaxNum(); rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null || row.getFirstCellNum() == NULL_CELL_NUM) {
                // 行内不包含任何单元格，则跳过
                continue;
            }
            T result = info.getClazz().getDeclaredConstructor().newInstance();
            for (int i = 0; i < info.getFields().size(); i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    continue;
                }
                List<Object> fieldExcels = info.getFields().get(i);
                Field field = ExcelImportInfo.getField(fieldExcels);
                ParseType type = ParseType.getFieldType(field);
                ExcelProperty excelProperty = ExcelImportInfo.getExcel(fieldExcels);
                PictureData pictureData = pictureDates.get(this.buildCoordinateKey(rowNum, i));
                Object cellValue = this.getCellValue(cell, type);
                try {
                    excelProperty.importHandler().getDeclaredConstructor().newInstance()
                            // 如果有图片，则优先处理图片
                            .exec(result, field, excelProperty, pictureData != null ? pictureData : cellValue);
                } catch (InstantiationException
                        | IllegalAccessException
                        | InvocationTargetException
                        | NoSuchMethodException
                        | IOException e) {
                    throw new RuntimeException(e.getLocalizedMessage(), e);
                }
            }
            results.add(result);
        }
        return results;
    }

    /**
     * excel 文件类型
     */
    private ExcelType excelType;

    /**
     * 获取工作薄
     *
     * @param filename    文件名称
     * @param inputStream inputStream
     * @return 工作薄
     * @throws IOException IOException
     */
    private Workbook getWorkbook(String filename, InputStream inputStream) throws IOException {
        // 判断文件是否是 excel 文件
        if (filename.endsWith(ExcelType.XLS.name().toLowerCase())) {
            excelType = ExcelType.XLS;
        } else if (filename.endsWith(ExcelType.XLSX.name().toLowerCase())) {
            excelType = ExcelType.XLSX;
        } else {
            throw new RuntimeException("上传文件不是一个 excel 文件");
        }
        Workbook workbook;
        try {
            switch (excelType) {
                case XLS:
                    workbook = new HSSFWorkbook(inputStream);
                    break;
                case XLSX:
                default:
                    workbook = new XSSFWorkbook(inputStream);
                    break;
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return workbook;
    }

    /**
     * 获取工作薄
     *
     * @param file 文件
     * @return 工作薄
     * @throws IOException IOException
     */
    private Workbook getWorkbook(File file) throws IOException {
        // 获得文件名
        if (file == null || !file.exists() || file.isDirectory() || file.length() == 0) {
            throw new RuntimeException("请上传 excel 文件");
        }
        // 判断文件是否是 excel 文件
        if (file.getName().endsWith(ExcelType.XLS.name().toLowerCase())) {
            excelType = ExcelType.XLS;
        } else if (file.getName().endsWith(ExcelType.XLSX.name().toLowerCase())) {
            excelType = ExcelType.XLSX;
        } else {
            throw new RuntimeException("上传文件不是一个 excel 文件");
        }
        Workbook workbook;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            switch (excelType) {
                case XLS:
                    workbook = new HSSFWorkbook(inputStream);
                    break;
                case XLSX:
                default:
                    workbook = new XSSFWorkbook(inputStream);
                    break;
            }
        }
        return workbook;
    }

    /**
     * 获取列值
     *
     * @param cell 列
     * @param type 字段类型
     * @return 获取列值
     */
    private Object getCellValue(Cell cell, ParseType type) {
        Object cellValue = null;
        // 判断数据的类型
        switch (cell.getCellType()) {
            // 数字
            case NUMERIC:
                // 公式
            case FORMULA:
                cellValue = cell.getNumericCellValue();
                if (DateUtil.isCellDateFormatted(cell)) {
                    // POI Excel 日期格式转换
                    switch (type) {
                        case DATE:
                            cellValue = DateUtil.getJavaDate((Double) cellValue);
                            break;
                        case LOCAL_DATE:
                            cellValue = DateUtil.getLocalDateTime((Double) cellValue).toLocalDate();
                            break;
                        case LOCAL_TIME:
                            cellValue = DateUtil.getLocalDateTime((Double) cellValue).toLocalTime();
                            break;
                        case LOCAL_DATE_TIME:
                        default:
                            cellValue = DateUtil.getLocalDateTime((Double) cellValue);
                            break;
                    }
                } else {
                    if ((Double) cellValue % 1 != 0) {
                        cellValue = new BigDecimal(cellValue.toString());
                    } else {
                        cellValue = new DecimalFormat("0").format(cellValue);
                    }
                }
                break;
            // 字符串
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            // 布尔
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            // 故障
            case ERROR:
                cellValue = cell.getErrorCellValue();
                break;
            // 空值
            case BLANK:
            default:
                break;
        }
        return cellValue;
    }

    /**
     * 坐标 KEY 格式
     */
    private static final String COORDINATE_KEY_FORMAT = "{0}-{1}";

    /**
     * 获取 坐标 KEY
     *
     * @param x X 轴
     * @param y Y 轴
     * @return 获取 坐标 KEY
     */
    private String buildCoordinateKey(Object x, Object y) {
        return MessageFormat.format(COORDINATE_KEY_FORMAT, x, y);
    }

    /**
     * 获取图片列表（XLS 格式）
     *
     * @param workbook 工作薄
     * @param sheet    工作表
     * @return 获取图片列表
     */
    private Map<String, PictureData> getPictureDataByXls(HSSFWorkbook workbook, HSSFSheet sheet) {
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        if (pictures.size() == 0) {
            return Collections.emptyMap();
        }
        Map<String, PictureData> sheetIndexPicMap = new HashMap<>(16);
        for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
            HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
            if (shape instanceof HSSFPicture) {
                HSSFPicture pic = (HSSFPicture) shape;
                int pictureIndex = pic.getPictureIndex() - 1;
                HSSFPictureData picData = pictures.get(pictureIndex);
                sheetIndexPicMap.put(this.buildCoordinateKey(anchor.getRow1(), anchor.getCol1()), picData);
            }
        }
        return sheetIndexPicMap;
    }

    /**
     * 获取图片列表（XLSX 格式）
     *
     * @param sheet 工作表
     * @return 获取图片列表
     */
    private Map<String, PictureData> getPictureDataByXlsx(XSSFSheet sheet) {
        Map<String, PictureData> sheetIndexPicMap = new HashMap<>(16);
        for (POIXMLDocumentPart dr : sheet.getRelations()) {
            if (dr instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) dr;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = pic.getPreferredSize();
                    CTMarker ctMarker = anchor.getFrom();
                    sheetIndexPicMap.put(this.buildCoordinateKey(ctMarker.getRow(), ctMarker.getCol()), pic.getPictureData());
                }
            }
        }
        return sheetIndexPicMap;
    }

}
