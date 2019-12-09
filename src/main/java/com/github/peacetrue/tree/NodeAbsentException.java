package com.github.peacetrue.tree;

import java.util.Objects;

/**
 * 树中不存在节点
 *
 * @author xiayx
 */
public class NodeAbsentException extends RuntimeException {

    private Object node;

    public NodeAbsentException(Object node) {
        super(String.format("树中不存在节点[%s]", node));
        this.node = Objects.requireNonNull(node);
    }

    @SuppressWarnings("unchecked")
    public <T> T getNode() {
        return (T) node;
    }
}
