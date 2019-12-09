package com.github.peacetrue.tree;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 泛化树，节点可使用任意类型，
 * 但需要通过{@link RootPredicate}指定根节点，{@link RelationPredicate}指定节点之间的关系.
 * <p>
 * 注意事项：
 * <ul>
 * <li>同一层级树节点的排序，等同于其添加入树中的顺序</li>
 * <li>不支持并发操作</li>
 * </ul>
 *
 * @author xiayx
 */
public class GenericTree<T> implements Tree<T> {

    private ConcurrentLinkedQueue<T> nodes;
    private RootPredicate<T> rootPredicate;
    private RelationPredicate<T> relationPredicate;

    /** 初始化空树，后续可通过 {@link #addNode(Object)} 添加节点 */
    public GenericTree(RootPredicate<T> rootPredicate, RelationPredicate<T> relationPredicate) {
        this.rootPredicate = Objects.requireNonNull(rootPredicate);
        this.relationPredicate = Objects.requireNonNull(relationPredicate);
        this.nodes = new ConcurrentLinkedQueue<>();
    }

    /** 初始化带节点的树 */
    public GenericTree(RootPredicate<T> rootPredicate, RelationPredicate<T> relationPredicate, Collection<T> nodes) {
        this(rootPredicate, relationPredicate);
        this.setNodes(nodes);
    }

    /** 检查指定节点是否存在于树中，不存在抛出异常 */
    private void checkNodeExist(T node) {
        if (!this.contains(node)) throw new NodeAbsentException(node);
    }

    @Override
    public Optional<T> getRoot() {
        return nodes.stream().filter(t -> rootPredicate.isRoot(t)).findAny();
    }

    @Override
    public boolean contains(T node) {
        return nodes.contains(node);
    }

    @Override
    public Optional<T> findParent(T node) {
        return nodes.stream().filter(t -> relationPredicate.isParentOf(t, node)).findAny();
    }

    /** 查找多个节点所有共同的父节点 */
    public List<T> findSameParents(Collection<T> nodes) {
        return findSame(nodes.stream().map(this::findParents).collect(Collectors.toSet()));
    }

    /** 查找多个节点最近共同的父节点 */
    public Optional<T> findSameParent(Collection<T> nodes) {
        List<T> parents = findSameParents(nodes);
        return parents.isEmpty() ? Optional.empty() : Optional.of(parents.get(parents.size() - 1));
    }

    private static <T> List<T> findSame(Collection<List<T>> lists) {
        Optional<Integer> min = lists.stream().map(List::size).min(Integer::compareTo);
        if (!min.isPresent()) return Collections.emptyList();

        List<T> same = new ArrayList<>(min.get());
        List<T> one = lists.iterator().next();
        for (int i = 0; i < min.get(); i++) {
            int _i = i;
            T element = one.get(i);
            if (lists.stream().map(list -> list.get(_i)).anyMatch(element::equals)) {
                same.add(element);
            } else {
                break;
            }
        }
        return same;
    }

    @Override
    public List<T> findChildren(T node) {
        return nodes.stream().filter(t -> relationPredicate.isChildOf(t, node)).collect(Collectors.toList());
    }

    @Override
    public List<T> findYounger(T node) {
        List<T> younger = new LinkedList<>();
        findYounger(younger, node);
        return younger;
    }

    private void findYounger(List<T> younger, T node) {
        findChildren(node).forEach(t -> {
            younger.add(t);
            findYounger(younger, t);
        });
    }

    @Override
    public Collection<T> getNodes() {
        return Collections.unmodifiableCollection(nodes);
    }

    @Override
    public void addNode(T node) {
        if (nodes.isEmpty()) {
            this.setRoot(node);
        } else if (nodes.contains(node)) {
            throw new NodeExistException(node);
        } else if (this.findParent(node).isPresent()) {
            nodes.add(node);
        } else {
            throw new ParentAbsentException(node);
        }
    }

    private void setRoot(T node) {
        if (rootPredicate.isRoot(Objects.requireNonNull(node))) {
            nodes.add(node);
        } else {
            throw new InvalidRootException(node);
        }
    }

    private void setNodes(Collection<T> nodes) {
        Objects.requireNonNull(nodes);
        this.nodes.clear();
        this.nodes.addAll(nodes);
        this.checkTreeValid();
    }

    /** 检查节点能否组成一颗有效树 */
    private void checkTreeValid() {
        Set<T> roots = nodes.stream().filter(t -> rootPredicate.isRoot(t)).collect(Collectors.toSet());
        if (roots.size() > 1) throw new MultiRootException(roots);
        if (roots.isEmpty()) throw new RootAbsentException();
        Set<T> nodes = new HashSet<>(this.nodes);
        nodes.removeAll(roots);
        nodes.forEach(t -> findParent(t).orElseThrow(() -> new ParentAbsentException(t)));
    }

    @Override
    public void removeNode(T node) {
        boolean remove = nodes.remove(node);
        if (remove) {
            nodes.removeAll(findYounger(node));
        } else {
            throw new NodeAbsentException(node);
        }
    }

    @Override
    public GenericTree<T> subtree(T node) {
        checkNodeExist(node);
        List<T> younger = findYounger(node);
        younger.add(0, node);
        return new GenericTree<>(root -> root.equals(node), relationPredicate, younger);
    }

    @Override
    public Tree<T> localTree(Collection<T> nodes) {
        nodes.forEach(this::checkNodeExist);
        Set<T> elders = nodes.stream().flatMap(t -> {
            List<T> parents = findParents(t);
            parents.add(t);
            return parents.stream();
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        return new GenericTree<>(rootPredicate, relationPredicate, elders);
    }

}
