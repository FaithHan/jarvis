package org.jarvis.redis.ratelimiter;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public abstract class RateLimiter {

    public double acquire() {
        return acquire(1);
    }

    public double acquire(int permits) {
        long from = Instant.now().toEpochMilli();
        tryAcquire(permits, -1, MILLISECONDS);
        return Instant.now().toEpochMilli() - from;
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0, MICROSECONDS);
    }

    public boolean tryAcquire(int permits) {
        return tryAcquire(permits, 0, MICROSECONDS);
    }

    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return tryAcquire(1, timeout, unit);
    }

    public abstract boolean tryAcquire(int permits, long timeout, TimeUnit unit);

    public abstract String getName();

}
