package com.pongsky.repository.utils.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应数据
 *
 * @author pengsenhao
 * @create 2021-02-11
 */
@Data
@NoArgsConstructor
public class PageResponse<T> {

    public PageResponse(List<T> content, PageQuery pageQuery, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
        this.pageNumber = pageQuery.getPageNumber();
        this.pageSize = pageQuery.getPageSize();
        this.totalPages = this.totalElements % this.pageSize == 0
                ? this.totalElements / this.pageSize
                : this.totalElements / this.pageSize + 1;
    }

    /**
     * 数据集合
     */
    private List<T> content;

    /**
     * 总页数
     */
    private Long totalPages;

    /**
     * 总数量
     */
    private Long totalElements;

    /**
     * 当前页码
     */
    private Integer pageNumber;

    /**
     * 页数量
     */
    private Integer pageSize;

}
