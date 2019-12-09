package com.github.peacetrue.tree;

import lombok.Getter;

import java.util.Objects;

/**
 * 树中已存在节点
 *
 * @author xiayx
 */
@Getter
public class NodeExistException extends RuntimeException {

    private Object node;

    public NodeExistException(Object node) {
        super(String.format("树中已存在节点[%s]", node));
        this.node = Objects.requireNonNull(node);
    }

}
