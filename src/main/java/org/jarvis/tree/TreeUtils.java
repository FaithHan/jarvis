package org.jarvis.tree;

import lombok.AllArgsConstructor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class TreeUtils<NODE, ID> {

    private String idName;

    private String parentIdName;

    private String childrenName;

    private Class<NODE> nodeClass;

    private Class<ID> idClass;

    public List<NODE> flat(List<NODE> treeList) {
        return flat(treeList, true);
    }

    public List<NODE> flat(List<NODE> treeList, boolean rewrite) {
        if (Objects.isNull(treeList) || treeList.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("tree list param not valid");
        }
        List<NODE> result = new LinkedList<>();
        Queue<NODE> queue = new LinkedList<>(treeList);
        while (!queue.isEmpty()) {
            NODE node = queue.poll();
            result.add(node);
            List<NODE> children = getChildren(node);
            children.forEach(queue::offer);
            if (rewrite) {
                setField(node, childrenName, null);
            }
        }
        return result;
    }

    public List<NODE> treeify(List<NODE> treeList) {
        if (Objects.isNull(treeList) || treeList.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("tree list param not valid");
        }
        Map<ID, List<NODE>> map = new HashMap<>();
        for (NODE node : treeList) {
            ID parentId = getField(node, parentIdName, idClass);
            map.computeIfAbsent(parentId, key -> new ArrayList<>()).add(node);
        }
        treeList.forEach(node -> setChildren(node, map.get(getField(node, idName, idClass))));
        return map.get(null);
    }

    /**
     * 返回本级及下级所有节点id
     *
     * @param flatTreeList
     * @param ids
     * @return
     */
    public List<ID> getCurrentAndChildrenIds(List<NODE> flatTreeList, Collection<ID> ids) {
        List<NODE> nodeList = findNodeList(flatTreeList, ids);
        Set<ID> result = new HashSet<>();
        Queue<NODE> queue = new LinkedList<>(nodeList);
        while (!queue.isEmpty()) {
            NODE node = queue.poll();
            ID id = getField(node, idName, idClass);
            if (result.add(id)) {
                queue.addAll(getChildren(node));
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * 获取所有父节点（不包括本级及下级节点）
     *
     * @param flatTreeList
     * @param ids
     * @return
     */
    public List<ID> getParentIds(List<NODE> flatTreeList, Collection<ID> ids) {
        Map<ID, NODE> idMap = flatTreeList.stream().collect(Collectors.toMap(node -> getField(node, idName, idClass), Function.identity()));
        List<NODE> nodeList = ids.stream().map(idMap::get).collect(Collectors.toList());
        List<ID> result = new ArrayList<>();
        for (NODE node : nodeList) {
            NODE current = idMap.get(getField(node, parentIdName, idClass));
            List<ID> parentIdList = new ArrayList<>();
            while (Objects.nonNull(current)) {
                parentIdList.add(getField(current, idName, idClass));
                current = idMap.get(getField(current, parentIdName, idClass));
            }
            if (parentIdList.stream().noneMatch(ids::contains)) {
                result.addAll(parentIdList);
            }
        }
        return result;
    }

    /**
     * 获取
     *
     * @param flatTreeList
     * @param ids
     * @return
     */
    public List<NODE> subTree(List<NODE> flatTreeList, Collection<ID> ids, String checkFieldName) {
        Set<ID> currentAndChildrenIds = new HashSet<>(getCurrentAndChildrenIds(flatTreeList, ids));
        Set<ID> parentIds = new HashSet<>(getParentIds(flatTreeList, ids));
        List<NODE> subList = flatTreeList.stream().filter(node -> {
            ID id = getField(node, idName, idClass);
            if (currentAndChildrenIds.contains(id) || parentIds.contains(id)) {
                if (checkFieldName != null) {
                    boolean check = currentAndChildrenIds.contains(id);
                    try {
                        Class<?> type = nodeClass.getDeclaredField(checkFieldName).getType();
                        if (type == Integer.class || type == Long.class) {
                            setField(node, checkFieldName, check ? 1 : 0);
                        } else if (type == Boolean.class) {
                            setField(node, checkFieldName, check);
                        } else if (type == String.class) {
                            setField(node, checkFieldName, check ? "1" : "0");
                        } else {
                            throw new RuntimeException("不支持的check类型");
                        }
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        return treeify(subList);
    }

    /**
     * 根据ID获取所有节点
     *
     * @param flatTreeList
     * @param ids
     * @return
     */
    public List<NODE> findNodeList(List<NODE> flatTreeList, Collection<ID> ids) {
        Map<ID, NODE> idMap = flatTreeList.stream().collect(Collectors.toMap(node -> getField(node, idName, idClass), Function.identity()));
        return ids.stream().map(idMap::get).collect(Collectors.toList());
    }

    private void setChildren(NODE node, List<NODE> childrenList) {
        try {
            Field field = nodeClass.getDeclaredField(childrenName);
            field.setAccessible(true);
            if (childrenList == null || List.class.isAssignableFrom(field.getType())) {
                field.set(node, childrenList);
            } else if (field.getType().isArray()) {
                NODE[] childrenArray = (NODE[]) Array.newInstance(nodeClass, childrenList.size());
                for (int i = 0; i < childrenArray.length; i++) {
                    childrenArray[i] = childrenList.get(i);
                }
                field.set(node, childrenArray);
            } else {
                throw new IllegalStateException("children只能是 list 或者 array");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<NODE> getChildren(NODE node) {
        try {
            Field field = nodeClass.getDeclaredField(childrenName);
            field.setAccessible(true);
            Object children = field.get(node);
            if (children == null) {
                return Collections.EMPTY_LIST;
            } else if (List.class.isAssignableFrom(field.getType())) {
                return (List<NODE>) children;
            } else if (field.getType().isArray()) {
                return Arrays.asList((NODE[]) children);
            } else {
                throw new IllegalStateException("children只能是 list 或者 array");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void setField(Object target, String fieldName, Object fieldValue) {
        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            declaredField.set(target, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static <T> T getField(Object target, String fieldName, Class<T> classValue) {
        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return classValue.cast(declaredField.get(target));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <NODE, ID> Builder<NODE, ID> builder(Class<NODE> nodeCLass, Class<ID> idClass) {
        return new Builder<>(nodeCLass, idClass);
    }

    @AllArgsConstructor(access = PRIVATE)
    public static class Builder<NODE, ID> {

        private String idName = "id";

        private String parentIdName = "parentId";

        private String childrenName = "children";

        private Class<NODE> nodeClass;

        private Class<ID> idClass;


        public Builder(Class<NODE> nodeClass, Class<ID> idClass) {
            this.nodeClass = nodeClass;
            this.idClass = idClass;
        }

        public TreeUtils.Builder<NODE, ID> withIdName(String idName) {
            this.idName = idName;
            return this;
        }

        public TreeUtils.Builder<NODE, ID> withParentIdName(String parentIdName) {
            this.parentIdName = parentIdName;
            return this;
        }

        public TreeUtils.Builder<NODE, ID> withChildrenName(String childrenName) {
            this.childrenName = childrenName;
            return this;
        }


        public <E> TreeUtils<NODE, ID> build() {
            return new TreeUtils<>(idName, parentIdName, childrenName, nodeClass, idClass);
        }
    }
}