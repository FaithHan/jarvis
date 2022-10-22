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
        Assert.notNull(list, "list must not be null");
        Assert.isTrue(size > 0, "size must greater than 0");
        this.list = list;
        this.size = Math.min(size, list.size());
    }

    public static <T> Partition<T> create(List<T> list, int size) {
        return new Partition<>(list, size);
    }

    @Override
    public List<T> get(int index) {
        int listSize = this.size();
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index " + index + " must not be negative");
        } else if (index >= listSize) {
            throw new IndexOutOfBoundsException("Index " + index + " must be less than size " + listSize);
        } else {
            int start = index * this.size;
            int end = Math.min(start + this.size, this.list.size());
            return this.list.subList(start, end);
        }
    }

    @Override
    public int size() {
        return (int) Math.ceil((double) this.list.size() / (double) this.size);
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }
}
