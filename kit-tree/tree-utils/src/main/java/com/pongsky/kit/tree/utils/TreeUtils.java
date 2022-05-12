package com.pongsky.kit.tree.utils;

import com.pongsky.kit.tree.entity.TreeNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 *
 * @author pengsenhao
 */
public class TreeUtils<T extends TreeNode> {

    /**
     * 默认顶层节点 ID
     */
    private static final String DEFAULT_ROOT_ID = "0";

    /**
     * 构建树形结构
     *
     * @param all 数据列表
     * @return 树形结构数据
     */
    public List<T> buildNode(List<T> all) {
        return this.buildNode(all, DEFAULT_ROOT_ID);
    }

    /**
     * 构建树形结构
     *
     * @param all    数据列表
     * @param rootId 顶层节点 ID
     * @return 树形结构数据
     */
    public List<T> buildNode(List<T> all, String rootId) {
        Map<String, List<T>> nodeGroup = all.stream()
                .collect(Collectors.groupingBy(TreeNode::getParentId));
        // 取出顶层节点
        List<T> results = nodeGroup.remove(rootId);
        // 递归构建子节点
        this.buildChildNode(results, nodeGroup);
        return results;
    }

    /**
     * 构建子节点树形结构
     *
     * @param results   顶层树形结构数据
     * @param nodeGroup 树形结构结构
     */
    private void buildChildNode(List<T> results, Map<String, List<T>> nodeGroup) {
        for (TreeNode result : results) {
            // 获取子节点列表
            List<T> children = nodeGroup.remove(result.getId());
            children = Optional.ofNullable(children).orElse(Collections.emptyList());
            result.setChildren(children);
            // 构建子孙节点树形结构
            this.buildChildNode(children, nodeGroup);
        }
    }

}
