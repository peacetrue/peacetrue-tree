package com.github.peacetrue.tree;

import java.util.List;
import java.util.Optional;

/**
 * 迭代树接口
 *
 * @author xiayx
 */
public interface IterableTree<T> {

    /**
     * 获取根节点，根节点没有父节点
     *
     * @return 根节点
     */
    Optional<T> getRoot();

    /**
     * 查找子节点
     *
     * @param node 节点
     * @return 子节点列表
     */
    List<T> findChildren(T node);

}
