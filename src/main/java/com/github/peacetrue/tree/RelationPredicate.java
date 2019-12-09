package com.github.peacetrue.tree;

/**
 * 节点间关系判断
 *
 * @param <T> 节点
 * @author xiayx
 */
public interface RelationPredicate<T> {

    /**
     * 节点parent是否节点child的父节点
     *
     * @param parent 父节点
     * @param child  子节点
     * @return 如果父节点是子节点的父亲，返回{@code true}；否则返回{@code false}
     */
    boolean isParentOf(T parent, T child);

    /**
     * 节点child是否节点parent的子节点
     *
     * @param child  子节点
     * @param parent 父节点
     * @return 如果子节点是父节点的儿子，返回{@code true}；否则返回{@code false}
     */
    default boolean isChildOf(T child, T parent) {
        return isParentOf(parent, child);
    }
}
