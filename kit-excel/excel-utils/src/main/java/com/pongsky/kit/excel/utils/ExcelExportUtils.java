package com.pongsky.kit.excel.utils;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import com.pongsky.kit.excel.annotation.ExcelPropertys;
import com.pongsky.kit.excel.annotation.content.ExcelContentStyle;
import com.pongsky.kit.excel.annotation.header.ExcelHeadAttr;
import com.pongsky.kit.excel.annotation.header.ExcelHeadStyle;
import com.pongsky.kit.excel.entity.ExcelExportInfo;
import com.pongsky.kit.excel.entity.ExcelForceInfo;
import com.pongsky.kit.type.parser.utils.FieldParserUtils;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        this(clazz, null, null);
    }

    public ExcelExportUtils(Class<?> clazz, List<String> topHeadAttrs, List<String> leftHeadAttrs) {
        info = new ExcelExportInfo()
                .setTopHeadAttrs(topHeadAttrs != null ? topHeadAttrs : Collections.emptyList())
                .setLeftHeadAttrs(leftHeadAttrs != null ? leftHeadAttrs : Collections.emptyList());
        info.setFields(new ArrayList<>());
        int topHeadMaxNum = 1;
        int leftHeadMaxNum = 1;
        List<Field> fields = FieldParserUtils.getSuperFields(clazz, true);
        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                topHeadMaxNum = Integer.max(topHeadMaxNum, excelProperty.topHeads().length);
                leftHeadMaxNum = Integer.max(leftHeadMaxNum, excelProperty.leftHeads().length);
                info.getFields().add(Arrays.asList(field, excelProperty));
            }
            ExcelPropertys excelPropertys = field.getAnnotation(ExcelPropertys.class);
            if (excelPropertys != null) {
                for (ExcelProperty ex : excelPropertys.value()) {
                    topHeadMaxNum = Integer.max(topHeadMaxNum, ex.topHeads().length);
                    leftHeadMaxNum = Integer.max(leftHeadMaxNum, ex.leftHeads().length);
                    info.getFields().add(Arrays.asList(field, ex));
                }
            }
        }
        info.getFields().sort(Comparator.comparing(f -> (ExcelExportInfo.getExcel(f).sort())));
        info.setWidths(new HashMap<>(info.getFields().size() * 2));
        topHeadMaxNum--;
        info.setTopHeadMaxNum(topHeadMaxNum);
        leftHeadMaxNum--;
        info.setLeftHeadMaxNum(leftHeadMaxNum);
    }

    /**
     * 列最大宽度
     */
    private static final int MAX_WIDTH = 255 * 256;

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
        int rowAccessWindowSize = Integer.max((info.getTopHeadMaxNum() + 1) + (info.getLeftHeadMaxNum() + 1),
                100);
        info.setRowAccessWindowSize(rowAccessWindowSize);
        info.setWorkbook(new SXSSFWorkbook(rowAccessWindowSize));
        info.getWorkbook().setCompressTempFiles(true);
        // 创建工作表
        info.setSheet(info.getWorkbook().createSheet(sheetName));
        // 创建绘图器
        info.setDrawing(info.getSheet().createDrawingPatriarch());
        // 构建 顶部 标题列表
        this.buildTopHeads();
        // 构建 左部 标题列表
        this.buildLeftHeads();
        // 构建 数据列表
        this.buildResults();
        // 宽度自适应
        for (int i = 0; i < info.getWidths().size(); i++) {
            Integer width = info.getWidths().get(i);
            if (width != null) {
                // 设置列宽有最大限度限制，如果超过最大限度时，则设置为最大限度
                info.getSheet().setColumnWidth(i, Integer.min(width, MAX_WIDTH));
            }
        }
        return info.getWorkbook();
    }

    /**
     * 高度自适应
     * <p>
     * 由于超过一定行数，行数会从内存刷新到硬盘，后续就无法读取/操作该行，所以再结束操作前设置高度
     *
     * @param startRow 开始行
     * @param endRow   结束行
     */
    private void autoSetHeight(int startRow, int endRow) {
        for (int i = startRow; i <= endRow; i++) {
            Short height = info.getHeights().get(i);
            if (height != null) {
                info.getSheet().getRow(i).setHeight(height);
            }
        }
    }

    /**
     * 保护单元格的密码
     */
    private static final String PROTECT_SHEET = "解锁";

    /**
     * 获取标题样式
     *
     * @param headStyle excel 标题样式
     * @return 获取标题样式
     */
    @SuppressWarnings("Duplicates")
    private CellStyle getHeadCellStyle(ExcelHeadStyle headStyle) {
        CellStyle cellStyle = info.getWorkbook().createCellStyle();
        cellStyle.setDataFormat(info.getWorkbook().createDataFormat().getFormat(headStyle.dataFormat()));
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(headStyle.alignment());
        cellStyle.setVerticalAlignment(headStyle.verticalAlignment());
        Font font = info.getWorkbook().createFont();
        font.setFontName(headStyle.fontName());
        font.setFontHeightInPoints(headStyle.fontSize());
        // 标题 加粗
        font.setBold(headStyle.isBold());
        // 标题 锁定
        if (headStyle.isLocked()) {
            info.getSheet().protectSheet(PROTECT_SHEET);
        }
        cellStyle.setLocked(headStyle.isLocked());
        // 标题 字体颜色
        font.setColor(headStyle.fontColor().getIndex());
        // 标题 边框样式、边框颜色
        cellStyle.setBorderTop(headStyle.borderTop());
        cellStyle.setTopBorderColor(headStyle.borderTopColor().getIndex());
        cellStyle.setBorderBottom(headStyle.borderBottom());
        cellStyle.setBottomBorderColor(headStyle.borderBottomColor().getIndex());
        cellStyle.setBorderLeft(headStyle.borderLeft());
        cellStyle.setLeftBorderColor(headStyle.borderLeftColor().getIndex());
        cellStyle.setBorderRight(headStyle.borderRight());
        cellStyle.setRightBorderColor(headStyle.borderRightColor().getIndex());
        // 标题 前景色
        cellStyle.setFillForegroundColor(headStyle.backgroundColor().getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    /**
     * 获取内容样式
     *
     * @param contentStyle excel 内容样式
     * @return 获取内容样式
     */
    @SuppressWarnings("Duplicates")
    private CellStyle getContentCellStyle(ExcelContentStyle contentStyle) {
        CellStyle cellStyle = info.getWorkbook().createCellStyle();
        cellStyle.setDataFormat(info.getWorkbook().createDataFormat().getFormat(contentStyle.dataFormat()));
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(contentStyle.alignment());
        cellStyle.setVerticalAlignment(contentStyle.verticalAlignment());
        Font font = info.getWorkbook().createFont();
        font.setFontName(contentStyle.fontName());
        font.setFontHeightInPoints(contentStyle.fontSize());
        // 内容 加粗
        font.setBold(contentStyle.isBold());
        // 内容 锁定
        if (contentStyle.isLocked()) {
            info.getSheet().protectSheet(PROTECT_SHEET);
        }
        cellStyle.setLocked(contentStyle.isLocked());
        // 内容 字体颜色
        font.setColor(contentStyle.fontColor().getIndex());
        // 内容 边框样式、边框颜色
        cellStyle.setBorderTop(contentStyle.borderTop());
        cellStyle.setTopBorderColor(contentStyle.borderTopColor().getIndex());
        cellStyle.setBorderBottom(contentStyle.borderBottom());
        cellStyle.setBottomBorderColor(contentStyle.borderBottomColor().getIndex());
        cellStyle.setBorderLeft(contentStyle.borderLeft());
        cellStyle.setLeftBorderColor(contentStyle.borderLeftColor().getIndex());
        cellStyle.setBorderRight(contentStyle.borderRight());
        cellStyle.setRightBorderColor(contentStyle.borderRightColor().getIndex());
        // 内容 前景色
        cellStyle.setFillForegroundColor(contentStyle.backgroundColor().getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
     * 构建 顶部 列名列表
     */
    private void buildTopHeads() {
        int maxX = 0;
        int maxY = 0;
        Map<String, String> coordinateMap
                = new HashMap<>((info.getTopHeadMaxNum() + 1) * info.getFields().size() * 2);
        // 循环 Y 轴
        for (int y = 0; y <= info.getTopHeadMaxNum(); y++) {
            Row row = null;
            // 循环 X 轴
            for (int x = 0; x < info.getFields().size(); x++) {
                List<Object> fieldExcels = info.getFields().get(x);
                Field field = ExcelExportInfo.getField(fieldExcels);
                ExcelProperty excelProperty = ExcelExportInfo.getExcel(fieldExcels);
                if (excelProperty.topHeads().length == 0) {
                    break;
                }
                if (row == null) {
                    row = info.getSheet().createRow(y);
                    info.setRow(row);
                }
                info.setCell(info.getRow().createCell(x));
                String columnName = field.getName();
                int z = 0;
                for (; z < excelProperty.topHeads().length; z++) {
                    columnName = excelProperty.topHeads()[z];
                    // 如果 Y 轴 相等，就不再往下获取，退出循环
                    if (z == y) {
                        break;
                    }
                }
                if (excelProperty.topHeadAttrs().length > 0) {
                    // 上面 for z 循环全走完最终会多 +1，进行去除
                    final int zz = z == excelProperty.topHeads().length && z > 0
                            ? z - 1
                            : z;
                    Optional<Integer> optional = Arrays.stream(excelProperty.topHeadAttrs())
                            .filter(a -> a.headIndex() == zz)
                            .map(ExcelHeadAttr::valueIndex)
                            .findAny();
                    if (optional.isPresent()) {
                        columnName = info.getTopHeadAttrs().get(optional.get());
                    }
                }
                // 设置样式
                info.getCell().setCellStyle(this.getHeadCellStyle(excelProperty.topHeadStyle()));
                // 填充内容
                info.getCell().setCellValue(new XSSFRichTextString(columnName));
                // 添加超链接
                if (StringUtils.isNotBlank(excelProperty.topHeadStyle().hyperlink())) {
                    this.addHyperlink(info.getCell(), excelProperty.topHeadStyle().hyperlink());
                }
                // 添加批注
                if (StringUtils.isNotBlank(excelProperty.topHeadStyle().comment())) {
                    this.addComment(info.getCell(), excelProperty.topHeadStyle().comment());
                }
                info.setTextWidth(x, columnName.length());
                // 记录 X、Y 轴对应列名信息
                coordinateMap.put(this.buildCoordinateKey(x, y), columnName);
                maxX = Integer.max(maxX, x);
                maxY = Integer.max(maxY, y);
            }
        }
        info.setRowNum(info.getTopHeadMaxNum() + 1);
        this.autoSetHeight(0, info.getTopHeadMaxNum());
        // 合并单元格
        this.forceCell(coordinateMap, 0, maxX, maxY);
    }

    /**
     * 构建 左部 列名列表
     */
    private void buildLeftHeads() {
        if (0 == info.getLeftHeadMaxNum()) {
            return;
        }
        int maxX = 0;
        int maxY = 0;
        Map<String, String> coordinateMap
                = new HashMap<>((info.getLeftHeadMaxNum() + 1) * info.getFields().size() * 2);
        // 循环 X 轴
        for (int y = info.getTopHeadMaxNum() + 1; y <= this.info.getFields().size() + info.getTopHeadMaxNum(); y++) {
            Row row = null;
            for (int x = 0; x <= info.getLeftHeadMaxNum(); x++) {
                List<Object> fieldExcels = info.getFields().get(y - info.getTopHeadMaxNum() - 1);
                Field field = ExcelExportInfo.getField(fieldExcels);
                ExcelProperty excelProperty = ExcelExportInfo.getExcel(fieldExcels);
                if (excelProperty.leftHeads().length == 0) {
                    break;
                }
                if (row == null) {
                    row = info.getSheet().createRow(y);
                    info.setRow(row);
                }
                info.setCell(info.getRow().createCell(x));
                String columnName = field.getName();
                int z = 0;
                for (; z < excelProperty.leftHeads().length; z++) {
                    columnName = excelProperty.leftHeads()[z];
                    // 如果 X 轴 相等，就不再往下获取，退出循环
                    if (z == x) {
                        break;
                    }
                }
                if (excelProperty.leftHeadAttrs().length > 0) {
                    // 上面 for z 循环全走完最终会多 +1，进行去除
                    final int zz = z == excelProperty.leftHeads().length && z > 0
                            ? z - 1
                            : z;
                    Optional<Integer> optional = Arrays.stream(excelProperty.leftHeadAttrs())
                            .filter(a -> a.headIndex() == zz)
                            .map(ExcelHeadAttr::valueIndex)
                            .findAny();
                    if (optional.isPresent()) {
                        columnName = info.getLeftHeadAttrs().get(optional.get());
                    }
                }
                // 设置样式
                info.getCell().setCellStyle(this.getHeadCellStyle(excelProperty.leftHeadStyle()));
                // 填充内容
                info.getCell().setCellValue(new XSSFRichTextString(columnName));
                // 添加超链接
                if (StringUtils.isNotBlank(excelProperty.leftHeadStyle().hyperlink())) {
                    this.addHyperlink(info.getCell(), excelProperty.leftHeadStyle().hyperlink());
                }
                // 添加批注
                if (StringUtils.isNotBlank(excelProperty.leftHeadStyle().comment())) {
                    this.addComment(info.getCell(), excelProperty.leftHeadStyle().comment());
                }
                info.setTextWidth(y, columnName.length());
                // 记录 X、Y 轴对应列名信息
                coordinateMap.put(this.buildCoordinateKey(x, y), columnName);
                maxX = Integer.max(maxX, x);
                maxY = Integer.max(maxY, y);
            }
        }
        info.setCellNum(info.getLeftHeadMaxNum() + 1);
        // 合并单元格
        this.forceCell(coordinateMap, info.getTopHeadMaxNum() + 1, maxX, maxY);
    }

    /**
     * 合并单元格
     * <p>
     * 程序写地有点乱，在这里讲解下合并思路：
     * <p>
     * 首先自上而下合并 Y 轴数据，如果相同则进行合并，并记录合并信息
     * 合并完后，自左而右尝试合并 X 轴数据
     * 1、如果合并不兼容，则进行回滚
     * 2、如果合并信息为新信息，则记录该合并信息
     * 3、如果合并信息为旧信息，则更新该合并信息
     * 4、如果合并信息能覆盖其他合并信息，则将多个合并信息合成一条，删除多余合并信息
     *
     * @param coordinateMap 坐标 MAP
     * @param startY        开始循环的 Y 轴 行数
     * @param maxX          X 轴 最大列数
     * @param maxY          Y 轴 最大行数
     */
    private void forceCell(Map<String, String> coordinateMap, int startY, int maxX, int maxY) {
        // 根据 X、Y 轴信息，分析出需要合并单元格的信息
        Map<String, ExcelForceInfo> forceInfoMap
                = new HashMap<>((info.getTopHeadMaxNum() + 1) * info.getFields().size() * 2);
        // 合并 Y 轴 单元格，并记录合并信息
        List<ExcelForceInfo> forceInfos = new ArrayList<>();
        for (int x = 0; x <= maxX; x++) {
            ExcelForceInfo forceInfo = null;
            for (int y = startY; y <= maxY; y++) {
                String last = coordinateMap.get(this.buildCoordinateKey(x, y));
                int nextY = y + 1;
                String next = coordinateMap.get(this.buildCoordinateKey(x, nextY));
                if (last == null) {
                    continue;
                } else if (!last.equals(next)) {
                    if (forceInfo != null) {
                        // 如果上一次有合并信息，还未添加，遇到不可合并的结果，则进行中止，开始下一轮循环
                        forceInfos.add(forceInfo);
                        forceInfo = null;
                    }
                    continue;
                }
                if (forceInfo == null) {
                    // 可合并，创建合并信息
                    forceInfo = new ExcelForceInfo(next, y, nextY, x, x);
                    // 备份合并信息
                    forceInfo.copy();
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
                    forceInfo.rollback();
                }
            }
            if (forceInfo != null) {
                // 如果刚好合并到最后一个单元格，退出循环，则保存合并信息
                forceInfos.add(forceInfo);
            }
        }
        for (int y = startY; y <= maxY; y++) {
            boolean isNewForce = false;
            ExcelForceInfo forceInfo = null;
            for (int x = 0; x <= maxX; x++) {
                String last = coordinateMap.get(this.buildCoordinateKey(x, y));
                int nextX = x + 1;
                String next = coordinateMap.get(this.buildCoordinateKey(nextX, y));
                if (last == null) {
                    continue;
                } else if (!last.equals(next)) {
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
                    forceInfo.copy();
                    // 是新地合并信息
                    isNewForce = true;
                    // 合并信息初始化
                    initForce = true;
                } else {
                    forceInfo.copy();
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
                    forceInfo.rollback();
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
            SXSSFRow row = info.getSheet().getRow(info.getRowNum());
            if (row != null && info.getRowAccessWindowSize() > info.getRowNum()) {
                info.setRow(row);
            } else {
                info.setRow(info.getSheet().createRow(info.getRowNum()));
            }
            for (int i = 0; i < info.getFields().size(); i++) {
                List<Object> fieldExcels = info.getFields().get(i);
                Field field = ExcelExportInfo.getField(fieldExcels);
                ExcelProperty excelProperty = ExcelExportInfo.getExcel(fieldExcels);
                if (!excelProperty.isExportData()) {
                    continue;
                }
                info.setCell(info.getRow().createCell(i + info.getCellNum()));
                info.getCell().setCellStyle(this.getContentCellStyle(excelProperty.contentStyle()));
                Object fieldValue = ParseResultUtils.parseFieldValue(field, excelProperty.attr(), result);
                try {
                    excelProperty.exportHandler().getDeclaredConstructor().newInstance()
                            .exec(field, excelProperty, fieldValue, info);
                } catch (InstantiationException
                        | IllegalAccessException
                        | InvocationTargetException
                        | NoSuchMethodException
                        | IOException e) {
                    throw new RuntimeException(e.getLocalizedMessage(), e);
                }
            }
            this.autoSetHeight(info.getRowNum(), info.getRowNum());
            info.rowNumPlusOne();
        }
    }

}
