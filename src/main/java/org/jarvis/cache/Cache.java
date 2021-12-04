package org.jarvis.cache;

import java.util.Optional;

public interface Cache<K, V> {

    Optional<V> get(K key);

    void remove(K key);

    int size();

    boolean isEmpty();

    void clear();

    void put(K key, V value);
}