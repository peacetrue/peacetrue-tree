package com.github.peacetrue.tree;

import lombok.Getter;

import java.util.Objects;

/**
 * 树中不存在节点
 *
 * @author xiayx
 */
@Getter
public class NodeAbsentException extends RuntimeException {

    private Object node;

    public NodeAbsentException(Object node) {
        super(String.format("树中不存在节点[%s]", node));
        this.node = Objects.requireNonNull(node);
    }

}
