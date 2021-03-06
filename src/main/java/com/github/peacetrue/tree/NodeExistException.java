package com.github.peacetrue.tree;

import java.util.Objects;

/**
 * 树中已存在节点
 *
 * @author xiayx
 */
public class NodeExistException extends RuntimeException {

    private Object node;

    public NodeExistException(Object node) {
        super(String.format("树中已存在节点[%s]", node));
        this.node = Objects.requireNonNull(node);
    }

    @SuppressWarnings("unchecked")
    public <T> T getNode() {
        return (T) node;
    }

}
