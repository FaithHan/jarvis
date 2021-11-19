package org.jarvis.redis.semaphore;

import org.jarvis.clock.Stopwatch;
import org.jarvis.clock.TimeUtils;
import org.jarvis.redis.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

class RedisSemaphoreTest {

    @Test
    void acquire() {
        StringRedisTemplate redisTemplate = RedisUtils.getStringRedisTemplate(6379);
        RedisSemaphore semaphore = new RedisSemaphore("hanfei", 10, redisTemplate);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 20; i++) {
            semaphore.acquire(5);
            System.out.println("acquire:" + i);
            System.out.println(stopwatch.elapsedSeconds());
        }
    }

    @Test
    void acquire_release() throws InterruptedException {
        StringRedisTemplate redisTemplate = RedisUtils.getStringRedisTemplate(6379);
        RedisSemaphore semaphore = new RedisSemaphore("hanfei", 10, 1000000, redisTemplate);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                semaphore.acquire(1);
                System.out.println("acquire:" + i);
                System.out.println(stopwatch.elapsedSeconds());
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                TimeUtils.sleep(1);
                semaphore.release(1);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}