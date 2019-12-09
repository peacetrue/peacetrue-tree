package com.github.peacetrue.tree;

import lombok.Getter;

import java.util.Collection;
import java.util.Objects;

/**
 * 节点集合中存在多个根节点
 *
 * @author xiayx
 */
@Getter
public class MultiRootException extends RuntimeException {

    private Collection<?> roots;

    public MultiRootException(Collection<?> roots) {
        super(String.format("节点集合中存在多个根节点[%s]", roots));
        this.roots = Objects.requireNonNull(roots);
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getRoots() {
        return (Collection<T>) roots;
    }
}
