package com.pongsky.kit.common.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求参数
 *
 * @author pengsenhao
 */
@Data
@NoArgsConstructor
public class PageQuery {

    public PageQuery(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * 默认 pageNumber
     */
    private static final int DEFAULT_PAGE_NUMBER = 1;

    /**
     * 当前页号
     */
    @ApiModelProperty("当前页号 最小 1")
    private Integer pageNumber;

    public Integer getPageNumber() {
        if (pageNumber != null && pageNumber >= DEFAULT_PAGE_NUMBER) {
            return pageNumber;
        }
        return DEFAULT_PAGE_NUMBER;
    }

    /**
     * 默认 pageSize
     */
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 最小 pageSize
     */
    private static final int MIN_PAGE_SIZE = 1;

    /**
     * 最大 pageSize
     */
    private static final int MAX_PAGE_SIZE = 200;

    /**
     * 一页数量
     */
    @ApiModelProperty("一页数量 最小 1 最大 200")
    private Integer pageSize;

    public Integer getPageSize() {
        if (pageSize != null
                && pageSize >= MIN_PAGE_SIZE
                && MAX_PAGE_SIZE >= pageSize) {
            return pageSize;
        }
        return DEFAULT_PAGE_SIZE;
    }

    /**
     * 偏移量
     *
     * @return 偏移量
     * @author pengsenhao
     */
    @ApiModelProperty(value = "偏移量", hidden = true)
    public Long getOffset() {
        return (long) (this.getPageNumber() - 1) * this.getPageSize();
    }

}
