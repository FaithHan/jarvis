package org.jarvis.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LFUCacheTest {

    @Test
    void lfuCacheTest() {
        LFUCache<String, String> lfuCache = new LFUCache<String, String>(3);
        lfuCache.put("1","1");
        lfuCache.put("2","2");
        lfuCache.put("3","3");
        System.out.println(lfuCache.get("1"));
        lfuCache.put("4","4");
        assertEquals(3,lfuCache.size());
    }
}