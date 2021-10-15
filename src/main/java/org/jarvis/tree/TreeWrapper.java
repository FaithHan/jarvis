package org.jarvis.tree;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jarvis.json.JsonUtils;
import org.jarvis.misc.Assert;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TreeWrapper<E> {

    private final String id;

    private final String parentId;

    private final String children;

    private final String weight;

    private final Class<E> classValue;

    private List<E> nodeList;

    private List<E> rootNodeList;

    private Map<Object, E> nodeMap;


    public static <E> TreeWrapper<E> create(List<E> nodeList) {
        return new Builder().build(nodeList);
    }

    public static Builder custom() {
        return new Builder();
    }

    private TreeWrapper(List<E> nodeList, String id, String parentId, String weight, String children) {
        Assert.notNull(nodeList, "node list can not be null");
        this.id = id;
        this.parentId = parentId;
        this.weight = weight;
        this.children = children;
        this.nodeList = new ArrayList<>(nodeList);
        this.classValue = (Class<E>) nodeList.get(0).getClass();
        sortNodeList(this.nodeList);
        build();
    }

    public List<E> getRootNodeList() {
        return rootNodeList;
    }

    public E getNodeById(Object id) {
        E element = this.nodeMap.get(id);
        if (element == null) {
            throw new IllegalArgumentException("没有对应的元素");
        }
        return element;
    }

    public E getRootNodeByTreeId(Object id) {
        E currentNode = nodeList.stream()
                .filter(node -> Objects.equals(id, getId(node)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("没有对应的元素"));
        while (true) {
            try {
                currentNode = getNodeById(getParentId(currentNode));
            } catch (Exception exception) {
                break;
            }
        }
        return currentNode;
    }

    public List<E> flatChildren(E node) {
        List<E> result = flatTree(node);
        result.remove(0);
        return result;
    }

    public List<E> flatTree(E node) {
        Queue<E> queue = new LinkedList<>();
        queue.add(node);
        List<E> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            E e = queue.poll();
            result.add(e);
            queue.addAll(getChildren(e));
        }
        return result;
    }

    private void sortNodeList(List<E> nodeList) {
        if (weight == null) {
            return;
        }
        nodeList.sort((o1, o2) -> {
            Object weight1 = getWeight(o1);
            Object weight2 = getWeight(o2);
            if (weight1 == null && weight2 == null) {
                return 0;
            } else if (weight1 == null) {
                return 1;
            } else if (weight2 == null) {
                return -1;
            } else {
                return ((Comparable) weight1).compareTo(weight2);
            }
        });
    }

    private void build() {
        for (E node : this.nodeList) {
            List<E> children = this.nodeList.stream()
                    .filter(child -> Objects.equals(getParentId(child), getId(node)))
                    .collect(Collectors.toList());
            setChildren(node, children);
        }

        HashMap<Object, E> nodeMap = new HashMap<>();

        for (E node : this.nodeList) {
            E oldNode = nodeMap.put(getId(node), node);
            if (oldNode != null) {
                throw new IllegalArgumentException("node list can not have duplicated elements");
            }
        }
        this.nodeMap = nodeMap;
        this.rootNodeList = this.nodeList.stream()
                .filter(node -> !this.nodeMap.containsKey(getParentId(node)))
                .collect(Collectors.toList());
    }

    private void rebuild() {
        destroy();
        build();
    }

    public void destroy() {
        nodeList.forEach(node -> setChildren(node, null));
        nodeMap = null;
        rootNodeList = null;
    }

    public String toJson() {
        return JsonUtils.toJson(this.rootNodeList);
    }

    private Object getId(E node) {
        return getValueByFieldName(node, id);
    }

    private Object getParentId(E node) {
        return getValueByFieldName(node, parentId);
    }

    private Object getWeight(E node) {
        return getValueByFieldName(node, weight);
    }

    private Object getValueByFieldName(E node, String fieldName) {
        try {
            Field field = classValue.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(node);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    private List<E> getChildren(E node) {
        try {
            Field field = classValue.getDeclaredField(children);
            field.setAccessible(true);
            Object children = field.get(node);
            if (children == null) {
                return Collections.EMPTY_LIST;
            } else if (List.class.isAssignableFrom(field.getType())) {
                return (List<E>) children;
            } else if (field.getType().isArray()) {
                return Arrays.asList((E[]) children);
            } else {
                throw new IllegalStateException("children只能是 list 或者 array");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    private void setChildren(E node, List<E> childrenList) {
        try {
            Field field = classValue.getDeclaredField(children);
            field.setAccessible(true);
            if (List.class.isAssignableFrom(field.getType())) {
                field.set(node, childrenList);
            } else if (field.getType().isArray()) {
                E[] childrenArray = (E[]) Array.newInstance(classValue, childrenList.size());
                for (int i = 0; i < childrenArray.length; i++) {
                    childrenArray[i] = childrenList.get(i);
                }
                field.set(node, childrenArray);
            } else {
                throw new IllegalStateException("children只能是 list 或者 array");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @NoArgsConstructor
    @AllArgsConstructor
    public static class Builder {
        private String id = "id";

        private String parentId = "parentId";

        private String weight = null;

        private String children = "children";

        public Builder withIdName(String idName) {
            this.id = idName;
            return this;
        }

        public Builder withParentIdName(String parentIdName) {
            this.parentId = parentIdName;
            return this;
        }

        public Builder withChildrenName(String childrenName) {
            this.children = childrenName;
            return this;
        }

        public Builder withWeightName(String weightName) {
            this.weight = weightName;
            return this;
        }

        public <E> TreeWrapper<E> build(List<E> nodeList) {
            return new TreeWrapper<>(nodeList, id, parentId, weight, children);
        }
    }

}
