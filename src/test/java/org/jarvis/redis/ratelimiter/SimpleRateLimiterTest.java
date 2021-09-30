package org.jarvis.redis.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;
import org.jarvis.clock.Stopwatch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleRateLimiterTest {

    @Test
    void acquire() {
        SimpleRateLimiter rateLimiter = new SimpleRateLimiter(1, 1);
//        RateLimiter rateLimiter = RateLimiter.create(100);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire(1);
        }
        System.out.println(stopwatch.elapsedMillis());
    }
}