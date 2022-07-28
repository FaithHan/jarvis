package org.jarvis.redis.distributedlock;

import org.jarvis.clock.Stopwatch;
import org.jarvis.clock.TimeUtils;
import org.jarvis.redis.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RedisLockTest {

    @Test
    void lock() throws InterruptedException {
        StringRedisTemplate redisTemplate = RedisUtils.getStringRedisTemplate(6379);
        RedisLock redisLock = new RedisLock(redisTemplate, "hanfei", 2000, 50);
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 3; i++) {
            threadPool.submit(() -> {
                try {
                    System.out.println("执行任务开始:" + Thread.currentThread().getId());
                    redisLock.lock();
                    TimeUtils.sleep(5);
                    System.out.println("执行任务完毕:" + Thread.currentThread().getId());
                    countDownLatch.countDown();
                } finally {
                    redisLock.unlock();
                }
            });
        }
        countDownLatch.await();
        System.out.print("总共花费：");
        System.out.println(stopwatch.elapsedSeconds());
    }

    @Test
    void name() {
        StringRedisTemplate redisTemplate = RedisUtils.getStringRedisTemplate(6379);
        RedisLock redisLock = new RedisLock(redisTemplate, "hanfei", 2000, 50);
        redisLock.tryLock();
        TimeUtils.sleep(10);
        redisLock.unlock();
    }
}