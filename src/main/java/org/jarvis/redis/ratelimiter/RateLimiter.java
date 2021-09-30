package org.jarvis.redis.ratelimiter;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MICROSECONDS;

public abstract class RateLimiter {

    public double acquire() {
        return acquire(1);
    }

    public abstract double acquire(int permits);

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
