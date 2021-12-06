package org.jarvis.cache;

import java.util.Optional;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public interface Cache<K, V> {

    Optional<V> get(K key);

    void remove(K key);

    int size();

    boolean isEmpty();

    void clear();

    void put(K key, V value);

    class CacheItem implements Delayed, Runnable {

        private final long expireTime;

        public CacheItem(long evictTime, TimeUnit unit) {
            this.expireTime = now() + unit.toMillis(evictTime);

        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expireTime - now(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed delayed) {
            return Long.compare(expireTime, ((CacheItem) delayed).expireTime);
        }

        private long now() {
            return System.currentTimeMillis();
        }

        @Override
        public void run() {

        }
    }
}