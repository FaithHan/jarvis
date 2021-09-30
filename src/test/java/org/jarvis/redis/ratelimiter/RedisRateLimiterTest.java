package org.jarvis.redis.ratelimiter;

import org.jarvis.redis.RedisUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RedisRateLimiterTest {
    @Test
    void name() throws InterruptedException {
        RedisRateLimiter redisRateLimiter = new RedisRateLimiter(1, 1, RedisUtils.getStringRedisTemplate("127.0.0.1", 6379), "test");
        long epochSecond = Instant.now().getEpochSecond();

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        CountDownLatch countDownLatch = new CountDownLatch(15);

        for (int i = 0; i < 15; i++) {

            executorService.submit(() -> {
                double acquire = redisRateLimiter.acquire(1);
                System.out.println(acquire);
                countDownLatch.countDown();
            });

        }
        countDownLatch.await();
        System.out.println("----------------");
        System.out.println(Instant.now().getEpochSecond() - epochSecond);

    }

    @Test
    void tryAcquireTest() throws InterruptedException {
        RedisRateLimiter redisRateLimiter =
                new RedisRateLimiter(100, 200, RedisUtils.getStringRedisTemplate("127.0.0.1", 6379), "test");
        boolean b = redisRateLimiter.tryAcquire(1);
        System.out.println(b);
    }


}