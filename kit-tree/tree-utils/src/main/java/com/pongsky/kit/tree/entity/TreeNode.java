package com.pongsky.kit.tree.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * 树形结构基础信息
 *
 * @author pengsenhao
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TreeNode {

    public TreeNode(String id, String parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private String id;

    /**
     * 父节点 ID
     */
    @ApiModelProperty("父节点 ID")
    private String parentId;

    /**
     * 子节点列表
     */
    @ApiModelProperty("子节点列表")
    private List<? extends TreeNode> children = Collections.emptyList();

}
