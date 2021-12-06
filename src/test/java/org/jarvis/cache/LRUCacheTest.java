package org.jarvis.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;

public class LRUCacheTest {

    private LRUCache<String, String> lruCache;

    @BeforeEach
    public void before() {
        lruCache = new LRUCache<>(5, 5, TimeUnit.SECONDS);
    }


    @Test
    void get() {
        lruCache.put("hehe1", "haha");
        lruCache.put("hehe2", "haha");
        lruCache.put("hehe3", "haha");
        lruCache.put("hehe4", "haha");
        lruCache.put("hehe5", "haha");
        lruCache.get("hehe1");
        lruCache.put("hehe6", "haha");
        System.out.println(lruCache.size());
        assertNull(lruCache.get("hehe2").orElse(null));
    }

    @Test
    void remove() {
    }
}