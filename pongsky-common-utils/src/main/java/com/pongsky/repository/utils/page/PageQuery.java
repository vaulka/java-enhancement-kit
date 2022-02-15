package com.pongsky.repository.utils.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求参数
 *
 * @author pengsenhao
 * @create 2021-02-11
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
     * 总数量
     */
    private Long pageCount;

    /**
     * 总页数
     */
    private Long pageLine;

    /**
     * 偏移量
     */
    @JsonIgnore
    public Long getOffset() {
        return (long) (getPageNumber() - 1) * getPageSize();
    }

    /**
     * 赋值总数量、总页数
     *
     * @param count 总数量
     */
    public void setPageCount(Long count) {
        if (count == null) {
            count = 0L;
        }
        pageCount = count;
        pageLine = pageCount % getPageSize() == 0
                ? pageCount / getPageSize()
                : pageCount / getPageSize() + 1;
    }

}
