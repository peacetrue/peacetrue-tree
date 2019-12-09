package com.github.peacetrue.tree;

import java.util.*;

/**
 * 树结构
 * <p>
 * 基本要求：
 * <ul>
 * <li>必须有且仅有一个根节点</li>
 * <li>树中的节点必须是唯一</li>
 * <li>根节点没有父节点，其他节点有且仅有一个父节点</li>
 * <li>每个节点可以有多个子节点</li>
 * </ul>
 * java的类继承结构是一颗典型的树，例如：
 * <pre>
 * class java.lang.Object
 * -class java.lang.Throwable
 * --class java.lang.Error
 * ---class java.lang.LinkageError
 * ---class java.lang.ThreadDeath
 * ---class java.lang.AssertionError
 * ---class java.lang.VirtualMachineError
 * --class java.lang.Exception
 * ---class java.lang.CloneNotSupportedException
 * ---class java.lang.ReflectiveOperationException
 * ---class java.lang.RuntimeException
 * ----class java.lang.IndexOutOfBoundsException
 * ----class java.lang.ArithmeticException
 * ----class java.lang.ClassCastException
 * ----class java.lang.NullPointerException
 * ----class java.lang.IllegalArgumentException
 * ---class java.lang.InterruptedException
 * </pre>
 * 方法说明会针对此示例进行举例。
 * <p>
 * 树的初始化逻辑：
 * <ul>
 * <li>构造空树</li>
 * <li>添加根节点</li>
 * <li>添加其他节点</li>
 * </ul>
 * <p>
 * 注意事项：
 * <ul>
 * <li>树中是否可以临时存储树外的节点：不可以，避免将问题复杂化。</li>
 * </ul>
 *
 * @author xiayx
 */
public interface Tree<T> extends IterableTree<T> {

    /**
     * 获取根节点，根节点没有父节点
     *
     * @return 根节点，没有根节点时返回{@link Optional#empty()}
     */
    @Override
    Optional<T> getRoot();

    /**
     * 获取所有节点
     *
     * @return 所有节点
     */
    Collection<T> getNodes();

    /**
     * 树中是否包含指定节点
     * <p>
     * 通过{@link #equals(Object)}方法比较两个节点是否相等
     *
     * @param node 节点
     * @return 如果包含返回 {@code true}，否则返回 {@code false}
     */
    boolean contains(T node);

    /**
     * 查找父节点
     * <p>
     * 除了根节点没有父节点，其他节点都有父节点。
     * <p>
     * 示例中：RuntimeException的父节点是Exception
     *
     * @param node 节点
     * @return 指定节点的父节点，指定节点不存在于树中或者根节点，返回{@link Optional#empty()}
     */
    Optional<T> findParent(T node);

    /**
     * 查找父辈节点，按根节点->子节点的顺序排列
     * <p>
     * 示例中：RuntimeException的父辈节点是[Object,Throwable,Exception]
     *
     * @param node 节点
     * @return 指定节点的父辈节点，指定节点不存在于树中或者根节点，返回空集合
     */
    default List<T> findParents(T node) {
        List<T> parents = new LinkedList<>();
        Optional<T> parent = Optional.of(node);
        while ((parent = findParent(parent.get())).isPresent()) {
            parents.add(parent.get());
        }
        Collections.reverse(parents);
        return parents;
    }

    /**
     * 查找子节点
     * <p>
     * 示例中：Throwable的子节点为[Error,Exception]
     *
     * @param node 节点
     * @return 指定节点的子节点，指定节点不存在于树中或者叶子节点，返回空集合
     */
    @Override
    List<T> findChildren(T node);

    /**
     * 查找所有子辈节点。
     * 不要求指定节点必须存在于树中。
     * <p>
     * 示例中：Throwable的子辈节点为：
     * <pre>
     * --class java.lang.Error
     * ---class java.lang.LinkageError
     * ---class java.lang.ThreadDeath
     * ---class java.lang.AssertionError
     * ---class java.lang.VirtualMachineError
     * --class java.lang.Exception
     * ---class java.lang.CloneNotSupportedException
     * ---class java.lang.ReflectiveOperationException
     * ---class java.lang.RuntimeException
     * ----class java.lang.IndexOutOfBoundsException
     * ----class java.lang.ArithmeticException
     * ----class java.lang.ClassCastException
     * ----class java.lang.NullPointerException
     * ----class java.lang.IllegalArgumentException
     * ---class java.lang.InterruptedException
     * </pre>
     *
     * @param node 节点
     * @return 指定节点的子辈节点，指定节点不存在于树中或者叶子节点，返回空集合
     */
    List<T> findYounger(T node);

    /**
     * 添加节点，根据指定节点的父节点进行关联。
     * <p>
     * 添加节点时，需要遵循从根节点到叶子节点的顺序依次添加
     * </p>
     *
     * @param node 节点
     * @throws NodeExistException    如果指定节点已存在于树中
     * @throws ParentAbsentException 如果指定节点的父节点不存在于树中
     */
    void addNode(T node) throws NodeExistException, ParentAbsentException;

    /**
     * 删除节点，同时删除所有子辈节点
     *
     * @param node 节点
     * @throws NodeAbsentException 如果指定节点不存在于树中
     */
    void removeNode(T node) throws NodeAbsentException;

    /**
     * 构造一颗以指定节点为根节点子树
     * <p>
     * 示例中：以Exception构造的子树为：
     * <pre>
     * --class java.lang.Exception
     * ---class java.lang.CloneNotSupportedException
     * ---class java.lang.ReflectiveOperationException
     * ---class java.lang.RuntimeException
     * ----class java.lang.IndexOutOfBoundsException
     * ----class java.lang.ArithmeticException
     * ----class java.lang.ClassCastException
     * ----class java.lang.NullPointerException
     * ----class java.lang.IllegalArgumentException
     * ---class java.lang.InterruptedException
     * </pre>
     *
     * @param node 节点
     * @return 子树
     * @throws NodeAbsentException 如果指定节点不存在于树中
     */
    Tree<T> subtree(T node) throws NodeAbsentException;

    /**
     * 构造一颗包含指定节点局部树
     * <p>
     * 示例中：以[Error,IndexOutOfBoundsException]构造的局部树为：
     * <pre>
     * class java.lang.Object
     * -class java.lang.Throwable
     * --class java.lang.Error
     * --class java.lang.Exception
     * ----class java.lang.IndexOutOfBoundsException
     * </pre>
     *
     * @param nodes 节点集合
     * @return 局部树
     * @throws NodeAbsentException 如果指定节点集合中含有不存在于树中节点
     */
    Tree<T> localTree(Collection<T> nodes) throws NodeAbsentException;

}
