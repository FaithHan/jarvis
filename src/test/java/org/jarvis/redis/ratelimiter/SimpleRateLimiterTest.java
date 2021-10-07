package org.jarvis.redis.ratelimiter;

import org.jarvis.clock.Stopwatch;
import org.junit.jupiter.api.Test;

class SimpleRateLimiterTest {

    @Test
    void acquire() {
        SimpleRateLimiter rateLimiter = new SimpleRateLimiter(1, 1);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire(1);
        }
        System.out.println(stopwatch.elapsedMillis());
    }
}