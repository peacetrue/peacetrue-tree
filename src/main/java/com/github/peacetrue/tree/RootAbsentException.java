package com.github.peacetrue.tree;

import lombok.Getter;

/**
 * 根节点不存在
 *
 * @author xiayx
 */
@Getter
public class RootAbsentException extends RuntimeException {

    public RootAbsentException() {
        super("根节点不存在");
    }

}
