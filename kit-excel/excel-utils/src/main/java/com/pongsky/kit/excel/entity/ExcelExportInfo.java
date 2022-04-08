package com.pongsky.kit.excel.entity;

import com.pongsky.kit.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 导出 excel 相关参数信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ExcelExportInfo {

    /**
     * field 与 excel 列表
     * <p>
     * List<List<Field,Excel>>
     */
    private List<List<Object>> fields;

    /**
     * field 下标
     */
    private static final int FIELD_INDEX = 0;

    /**
     * excel 下标
     */
    private static final int EXCEL_INDEX = 1;

    /**
     * 获取 field 信息
     *
     * @param fieldExcels fieldExcels
     * @return 获取 field 信息
     */
    public static Field getField(List<Object> fieldExcels) {
        return (Field) fieldExcels.get(FIELD_INDEX);
    }

    /**
     * 获取 excel 信息
     *
     * @param fieldExcels fieldExcels
     * @return 获取 excel 信息
     */
    public static ExcelProperty getExcel(List<Object> fieldExcels) {
        return (ExcelProperty) fieldExcels.get(EXCEL_INDEX);
    }

    /**
     * 单元格宽度信息
     * <p>
     * Map<X 轴,List<宽度>>
     */
    private Map<Integer, Integer> widths;

    /**
     * 单元格高度信息
     * <p>
     * Map<Y 轴,List<高度>>
     */
    private Map<Integer, Short> heights;

    /**
     * 获取文本对应的宽度
     *
     * @param length 文本长度
     * @return 获取文本对应的宽度
     */
    public int getTextWidth(int length) {
        return (length + 1) * 2 * 256;
    }

    /**
     * 获取图片对应的宽度
     *
     * @param width 图片宽度
     * @return 获取图片对应的宽度
     */
    public int getImageWidth(int width) {
        return width * 32;
    }

    /**
     * 设置文本宽度
     *
     * @param cell   列
     * @param length 文本长度
     */
    public void setTextWidth(int cell, int length) {
        int width = this.getTextWidth(length);
        this.setWidth(cell, width);
    }

    /**
     * 设置图片宽度
     *
     * @param cell  列
     * @param width 图片宽度
     */
    public void setImageWidth(int cell, int width) {
        width = this.getImageWidth(width);
        this.setWidth(cell, width);
    }

    /**
     * 设置宽度
     *
     * @param cell  列
     * @param width 宽度
     */
    public void setWidth(int cell, int width) {
        Integer oldWidth = widths.get(cell);
        if (oldWidth == null || width > oldWidth) {
            widths.put(cell, width);
        }
    }

    /**
     * 获取图片对应的高度
     *
     * @param height 图片高度
     * @return 获取图片对应的高度
     */
    public int getImageHeight(int height) {
        return height * 15;
    }

    /**
     * 设置图片高度
     *
     * @param row    行
     * @param height 图片高度
     */
    public void setImageHeight(int row, int height) {
        height = this.getImageHeight(height);
        this.setHeight(row, height);
    }

    /**
     * 设置高度
     *
     * @param row    行
     * @param height 高度
     */
    public void setHeight(int row, int height) {
        Short oldHeight = heights.get(row);
        if (oldHeight == null || height > oldHeight) {
            heights.put(row, (short) height);
        }
    }

    /**
     * 数据列表
     */
    private List<?> results;

    /**
     * 工作簿
     */
    private SXSSFWorkbook workbook;

    /**
     * 绘画器
     */
    private SXSSFDrawing drawing;

    /**
     * 工作表
     */
    private SXSSFSheet sheet;

    /**
     * 当前行
     */
    private Row row;

    /**
     * 当前列
     */
    private Cell cell;

    /**
     * 顶部 标题最大行数，从 0 开始
     */
    private int topHeadMaxNum;

    /**
     * 顶部 标题动态值列表
     */
    private List<String> topHeadAttrs;

    /**
     * 左部 标题最大行数，从 0 开始
     */
    private int leftHeadMaxNum;

    /**
     * 左部 标题动态值列表
     */
    private List<String> leftHeadAttrs;

    /**
     * 当前行号
     */
    private int rowNum = 0;

    public void rowNumPlusOne() {
        rowNum++;
    }

    /**
     * 当前列号
     */
    private int cellNum = 0;

    /**
     * 缓存到内存的行数
     */
    private Integer rowAccessWindowSize;

}
