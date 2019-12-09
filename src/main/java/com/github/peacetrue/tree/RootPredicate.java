package com.github.peacetrue.tree;

/**
 * 根节点判断
 *
 * @param <T> 节点
 * @author xiayx
 */
public interface RootPredicate<T> {
    /**
     * 指定节点是否根节点
     *
     * @param node 节点
     * @return 如果是根节点，返回{@code true}，，否则返回{@code false}
     */
    boolean isRoot(T node);
}
