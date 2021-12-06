package org.jarvis.cache;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

public class LFUCache<K, V> implements Cache<K, V> {

    private static final DelayQueue<LRUCache.CacheItem> EVICT_QUEUE = new DelayQueue<>();

    private final int capacity;

    private final long evictTime;

    private final TimeUnit timeUnit;

    private final Map<K, V> cache;

    private final Map<K, Integer> keyTimes = new HashMap<>();

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

    public LFUCache(int capacity) {
        this(capacity, 0, TimeUnit.MILLISECONDS);
    }

    public LFUCache(int capacity, long evictTime, TimeUnit timeUnit) {
        this.capacity = capacity;
        this.evictTime = evictTime;
        this.timeUnit = timeUnit;
        this.cache = new HashMap<>((int) Math.ceil(capacity / 0.75) + 1);
    }

    @Override
    public synchronized Optional<V> get(K key) {
        if (keyTimes.containsKey(key)) {
            int times = keyTimes.get(key);
            keyTimes.put(key, times + 1);
        }
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public synchronized void remove(K key) {
        cache.remove(key);
        keyTimes.remove(key);
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
        if (this.size() >= capacity) {
            keyTimes.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .ifPresent(entry -> {
                        K evictKey = entry.getKey();
                        cache.remove(evictKey);
                        keyTimes.remove(evictKey);
                    });

        }
        cache.put(key, value);
        keyTimes.put(key, 0);
        if (evictTime > 0) {
            EVICT_QUEUE.put(new CacheItem(evictTime, timeUnit) {
                @Override
                public void run() {
                    LFUCache.this.remove(key);
                }
            });
        }
    }
}
