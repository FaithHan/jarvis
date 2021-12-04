package org.jarvis.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class LRUCache<K, V> implements Cache<K, V> {

    private static final DelayQueue<CacheItem> EVICT_QUEUE = new DelayQueue<>();

    private final int capacity;

    private final long evictTime;

    private final TimeUnit timeUnit;

    private final Map<K, V> cache;

    static {
        new Thread() {
            {
                setDaemon(true);
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        EVICT_QUEUE.take().run();
                    } catch (InterruptedException ignored) {

                    }
                }
            }
        }.start();
    }


    public LRUCache(int capacity) {
        this(capacity, 0, TimeUnit.MILLISECONDS);
    }

    public LRUCache(int capacity, long evictTime, TimeUnit timeUnit) {
        this.capacity = capacity;
        this.evictTime = evictTime;
        this.timeUnit = timeUnit;
        this.cache = new LinkedHashMap<K, V>((int) Math.ceil(capacity / 0.75) + 1, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }

    @Override
    public synchronized Optional<V> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public synchronized void remove(K key) {
        cache.remove(key);
    }

    @Override
    public synchronized int size() {
        return cache.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public synchronized void clear() {
        cache.clear();
    }

    @Override
    public synchronized void put(K key, V value) {
        cache.put(key, value);
        if (evictTime > 0) {
            EVICT_QUEUE.put(new CacheItem(evictTime, timeUnit) {
                @Override
                public void run() {
                    LRUCache.this.remove(key);
                }
            });
        }
    }

    private static class CacheItem implements Delayed, Runnable {

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
