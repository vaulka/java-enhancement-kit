package com.pongsky.kit.excel.utils;

import com.pongsky.kit.excel.annotation.Excel;
import com.pongsky.kit.excel.annotation.Excels;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import com.pongsky.kit.excel.entity.ExcelForceInfo;
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
import org.apache.poi.ss.util.CellRangeAddress;
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
        int titleMaxNum = 1;
        for (Class<?> cla : classes) {
            List<Field> fieldList = Arrays.stream(cla.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Excel.class) || f.isAnnotationPresent(Excels.class))
                    .collect(Collectors.toList());
            for (Field field : fieldList) {
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null) {
                    titleMaxNum = Integer.max(titleMaxNum, excel.value().length);
                    info.getFields().add(Arrays.asList(field, excel));
                }
                Excels excels = field.getAnnotation(Excels.class);
                if (excels != null) {
                    for (Excel ex : excels.value()) {
                        titleMaxNum = Integer.max(titleMaxNum, ex.value().length);
                        info.getFields().add(Arrays.asList(field, ex));
                    }
                }
            }
        }
        info.getFields().sort(Comparator.comparing(f -> (ExcelExportInfo.getExcel(f).sort())));
        info.setWidths(new HashMap<>(info.getFields().size() * 2));
        titleMaxNum--;
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
            // 列名 字体颜色
            font.setColor(excel.fontColor().getIndex());
            // 列名 前景色
            cellStyle.setFillForegroundColor(excel.backgroundColor().getIndex());
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
    private String buildCoordinateKey(int x, int y) {
        return MessageFormat.format(COORDINATE_KEY_FORMAT, x, y);
    }

    /**
     * 构建 列名列表
     */
    private void buildColumnTitles() {
        int maxX = 0;
        int maxY = 0;
        Map<String, String> coordinateMap
                = new HashMap<>((info.getTitleMaxNum() + 1) * info.getFields().size() * 2);
        // 循环 Y 轴
        for (int y = 0; y <= info.getTitleMaxNum(); y++) {
            info.setRow(info.getSheet().createRow(y));
            // 循环 X 轴
            for (int x = 0; x < info.getFields().size(); x++) {
                info.setCell(info.getRow().createCell(x));
                List<Object> fieldExcels = info.getFields().get(x);
                Field field = ExcelExportInfo.getField(fieldExcels);
                Excel excel = ExcelExportInfo.getExcel(fieldExcels);
                String columnName = field.getName();
                for (int z = 0; z < excel.value().length; z++) {
                    if (StringUtils.isBlank(excel.value()[z])) {
                        continue;
                    }
                    columnName = excel.value()[z];
                    // 如果 Y 轴 相等，就不再往下获取，退出循环
                    if (z == y) {
                        break;
                    }
                }
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
                info.setTextWidth(x, columnName.length());
                // 记录 X、Y 轴对应列名信息
                coordinateMap.put(this.buildCoordinateKey(x, y), columnName);
                maxX = Integer.max(maxX, x);
                maxY = Integer.max(maxY, y);
            }
            info.rowNumPlusOne();
        }
        // 合并单元格
        this.forceCell(coordinateMap, maxX, maxY);
    }

    /**
     * 合并单元格
     * <p>
     * 程序写的有点乱，在这里讲解下合并思路：
     * <p>
     * 首先自上而下合并 Y 轴数据，如果相同则进行合并，并记录合并信息
     * 合并完后，自左而右尝试合并 X 轴数据
     * 1、如果合并不兼容，则进行回滚
     * 2、如果合并信息为新信息，则记录该合并信息
     * 3、如果合并信息为旧信息，则更新该合并信息
     * 4、如果合并信息能覆盖其他合并信息，则将多个合并信息合成一条，删除多余合并信息
     *
     * @param coordinateMap 坐标 MAP
     * @param maxX          X 轴 最大列数
     * @param maxY          Y 轴 最大行数
     */
    private void forceCell(Map<String, String> coordinateMap, int maxX, int maxY) {
        // 根据 X、Y 轴信息，分析出需要合并单元格的信息
        Map<String, ExcelForceInfo> forceInfoMap
                = new HashMap<>((info.getTitleMaxNum() + 1) * info.getFields().size() * 2);
        // 合并 Y 轴 单元格，并记录合并信息
        List<ExcelForceInfo> forceInfos = new ArrayList<>();
        for (int x = 0; x <= maxX; x++) {
            ExcelForceInfo originalForceInfo = null;
            ExcelForceInfo forceInfo = null;
            for (int y = 0; y <= maxY; y++) {
                String last = coordinateMap.get(this.buildCoordinateKey(x, y));
                int nextY = y + 1;
                String next = coordinateMap.get(this.buildCoordinateKey(x, nextY));
                if (!last.equals(next)) {
                    if (forceInfo != null) {
                        // 如果上一次有合并信息，还未添加，遇到不可合并的结果，则进行中止，开始下一轮循环
                        forceInfos.add(forceInfo);
                        originalForceInfo = null;
                        forceInfo = null;
                    }
                    continue;
                }
                if (forceInfo == null) {
                    // 可合并，创建合并信息
                    forceInfo = new ExcelForceInfo(next, y, nextY, x, x);
                    // 备份合并信息
                    originalForceInfo = forceInfo.copy();
                } else {
                    forceInfo
                            .setColumnTitle(next)
                            .setStartRow(Integer.min(forceInfo.getStartRow(), y))
                            .setEndRow(Integer.max(forceInfo.getEndRow(), nextY))
                            .setStartCell(Integer.min(forceInfo.getStartCell(), x))
                            .setEndCell(Integer.max(forceInfo.getEndCell(), x));
                }
                boolean isSuccess = this.validationForce(coordinateMap, forceInfoMap, forceInfo);
                if (!isSuccess) {
                    // 不可合并，回滚合并信息
                    forceInfo.rollback(originalForceInfo);
                }
            }
            if (forceInfo != null) {
                // 如果刚好合并到最后一个单元格，退出循环，则保存合并信息
                forceInfos.add(forceInfo);
            }
        }
        for (int y = 0; y <= maxY; y++) {
            boolean isNewForce = false;
            ExcelForceInfo originalForceInfo;
            ExcelForceInfo forceInfo = null;
            for (int x = 0; x <= maxX; x++) {
                String last = coordinateMap.get(this.buildCoordinateKey(x, y));
                int nextX = x + 1;
                String next = coordinateMap.get(this.buildCoordinateKey(nextX, y));
                if (!last.equals(next)) {
                    if (forceInfo != null && isNewForce) {
                        // 如果上一次有合并信息，还未添加，遇到不可合并的结果，则进行中止，开始下一轮循环
                        forceInfos.add(forceInfo);
                        isNewForce = false;
                        forceInfo = null;
                    }
                    continue;
                }
                // 首先获取该坐标是否已有合并记录，如果有，则复用合并记录，如果无，则创建合并记录
                forceInfo = forceInfoMap.get(this.buildCoordinateKey(x, y));
                ExcelForceInfo nextForceInfo = forceInfoMap.get(this.buildCoordinateKey(nextX, y));
                boolean initForce = false;
                if (forceInfo == null) {
                    // 可合并，创建合并信息
                    forceInfo = new ExcelForceInfo(next, y, y, x, nextX);
                    // // 备份合并信息
                    originalForceInfo = forceInfo.copy();
                    // 是新的合并信息
                    isNewForce = true;
                    // 合并信息初始化
                    initForce = true;
                } else {
                    originalForceInfo = forceInfo.copy();
                    forceInfo
                            .setStartRow(Integer.min(forceInfo.getStartRow(), y))
                            .setEndRow(Integer.max(forceInfo.getEndRow(), y))
                            .setStartCell(Integer.min(forceInfo.getStartCell(), x))
                            .setEndCell(Integer.max(forceInfo.getEndCell(), nextX));
                }
                if (nextForceInfo != null) {
                    forceInfo
                            .setStartRow(Integer.min(forceInfo.getStartRow(), nextForceInfo.getStartRow()))
                            .setEndRow(Integer.max(forceInfo.getEndRow(), nextForceInfo.getEndRow()))
                            .setStartCell(Integer.min(forceInfo.getStartCell(), nextForceInfo.getStartCell()))
                            .setEndCell(Integer.max(forceInfo.getEndCell(), nextForceInfo.getEndCell()));
                }
                boolean isSuccess = this.validationForce(coordinateMap, forceInfoMap, forceInfo);
                if (!isSuccess) {
                    forceInfo.rollback(originalForceInfo);
                    // 如果刚初始化完合并记录合并结果不通过，则放弃该合并记录
                    // 只有不是初始化并且是新合并记录才能添加进去
                    if (!initForce && isNewForce) {
                        forceInfos.add(forceInfo);
                    }
                    isNewForce = false;
                    forceInfo = null;
                } else if (nextForceInfo != null && forceInfo != nextForceInfo) {
                    // 前一个合并记录刚好能覆盖下一个合并记录，则将两个合并记录合为一个，删除掉下一个合并记录
                    forceInfos.remove(nextForceInfo);
                }
            }
            if (forceInfo != null && isNewForce) {
                // 如果刚好合并到最后一个单元格，退出循环，则保存合并信息
                forceInfos.add(forceInfo);
            }
        }
        // 合并单元格
        for (ExcelForceInfo forceInfo : forceInfos) {
            CellRangeAddress region = new CellRangeAddress(forceInfo.getStartRow(), forceInfo.getEndRow(),
                    forceInfo.getStartCell(), forceInfo.getEndCell());
            info.getSheet().addMergedRegion(region);
        }
    }

    /**
     * 校验是否可合并
     *
     * @param coordinateMap 坐标 MAP
     * @param forceInfoMap  合并信息 MAP
     * @param forceInfo     合并待校验信息
     * @return 是否可合并结果
     */
    private boolean validationForce(Map<String, String> coordinateMap,
                                    Map<String, ExcelForceInfo> forceInfoMap,
                                    ExcelForceInfo forceInfo) {
        boolean isSuccess = true;
        here:
        for (int y = forceInfo.getEndRow(); y >= forceInfo.getStartRow(); y--) {
            for (int x = forceInfo.getEndCell(); x >= forceInfo.getStartCell(); x--) {
                String key = this.buildCoordinateKey(x, y);
                String name = coordinateMap.get(key);
                if (!forceInfo.getColumnTitle().equals(name)) {
                    isSuccess = false;
                    break here;
                }
            }
        }
        if (isSuccess) {
            for (int x = forceInfo.getEndCell(); x >= forceInfo.getStartCell(); x--) {
                for (int y = forceInfo.getEndRow(); y >= forceInfo.getStartRow(); y--) {
                    String key = this.buildCoordinateKey(x, y);
                    forceInfoMap.put(key, forceInfo);
                }
            }
        }
        return isSuccess;
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
