package org.jarvis.id;

/**
 * https://zhuanlan.zhihu.com/p/107939861
 *
 * @param <T>
 */
public interface IdGenerator<T> {

    T nextId();

}
