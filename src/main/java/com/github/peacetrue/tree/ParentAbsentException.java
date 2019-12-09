package com.github.peacetrue.tree;

import java.util.Objects;

/**
 * 树中不存在节点的父节点
 *
 * @author xiayx
 */
public class ParentAbsentException extends RuntimeException {

    private Object node;

    public ParentAbsentException(Object node) {
        super(String.format("节点[%s]的父节点不存在于树中", node));
        this.node = Objects.requireNonNull(node);
    }

    @SuppressWarnings("unchecked")
    public <T> T getNode() {
        return (T) node;
    }

}
