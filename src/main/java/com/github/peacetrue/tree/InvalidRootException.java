package com.github.peacetrue.tree;

import java.util.Objects;

/**
 * 根节点无效异常
 *
 * @author xiayx
 */
public class InvalidRootException extends RuntimeException {

    private Object node;

    public InvalidRootException(Object node) {
        super(String.format("[%s]不是根节点", node));
        this.node = Objects.requireNonNull(node);
    }

    @SuppressWarnings("unchecked")
    public <T> T getNode() {
        return (T) node;
    }

}
