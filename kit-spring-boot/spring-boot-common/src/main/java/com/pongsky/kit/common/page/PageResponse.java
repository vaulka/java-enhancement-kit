package com.pongsky.kit.common.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应数据
 *
 * @author pengsenhao
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
    @ApiModelProperty("数据集合")
    private List<T> content;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private Long totalPages;

    /**
     * 总数量
     */
    @ApiModelProperty("总数量")
    private Long totalElements;

    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private Integer pageNumber;

    /**
     * 页数量
     */
    @ApiModelProperty("页数量")
    private Integer pageSize;

}
