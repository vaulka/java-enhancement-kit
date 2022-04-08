package com.pongsky.kit.excel.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * excel 单元格合并信息
 *
 * @author pengsenhao
 **/
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ExcelForceInfo {

    public ExcelForceInfo(String columnTitle, Integer startRow, Integer endRow, Integer startCell, Integer endCell) {
        this.columnTitle = columnTitle;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCell = startCell;
        this.endCell = endCell;
    }

    public void copy() {
        this.copy = new ExcelForceInfo(this.columnTitle, this.startRow, this.endRow, this.startCell, this.endCell);
    }

    public void rollback() {
        this.columnTitle = this.copy.columnTitle;
        this.startRow = this.copy.startRow;
        this.endRow = this.copy.endRow;
        this.startCell = this.copy.startCell;
        this.endCell = this.copy.endCell;
    }

    /**
     * 列名
     */
    private String columnTitle;

    /**
     * 开始行
     */
    private Integer startRow;

    /**
     * 结束行
     */
    private Integer endRow;

    /**
     * 开始列
     */
    private Integer startCell;

    /**
     * 结束列
     */
    private Integer endCell;

    /**
     * 副本
     */
    private ExcelForceInfo copy;

}
