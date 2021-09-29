package org.jarvis.redis.ratelimiter;

import org.jarvis.misc.Stopwatch;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public abstract class RateLimiter {

    public double acquire() {
        return acquire(1);
    }

    public double acquire(int permits) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        tryAcquire(permits, -1, MILLISECONDS);
        return stopwatch.elapsedMillis();
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
