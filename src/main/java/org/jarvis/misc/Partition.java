package org.jarvis.misc;

import java.util.AbstractList;
import java.util.List;

public class Partition<T> extends AbstractList<List<T>> {

    private final List<T> list;
    private final int size;

    /**
     * 列表分区
     *
     * @param list 被分区的列表
     * @param size 每个分区的长度
     */
    private Partition(List<T> list, int size) {
        this.list = list;
        this.size = Math.min(size, list.size());
    }

    public static <E> Partition<E> partition(List<E> list, int size) {
        return new Partition<>(list, size);
    }

    @Override
    public List<T> get(int index) {
        int start = index * size;
        int end = Math.min(start + size, list.size());
        return list.subList(start, end);
    }

    @Override
    public int size() {
        // 此处采用动态计算，以应对list变
        final int size = this.size;
        final int total = list.size();
        int length = total / size;
        if (total % size > 0) {
            length += 1;
        }
        return length;
    }

}
