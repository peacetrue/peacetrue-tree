package com.github.peacetrue.tree;

import lombok.Getter;

import java.util.Objects;

/**
 * 根节点无效异常
 *
 * @author xiayx
 */
@Getter
public class InvalidRootException extends RuntimeException {

    private Object node;

    public InvalidRootException(Object node) {
        super(String.format("[%s]不是根节点", node));
        this.node = Objects.requireNonNull(node);
    }

}
