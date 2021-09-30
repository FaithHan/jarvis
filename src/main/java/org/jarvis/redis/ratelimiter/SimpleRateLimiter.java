package org.jarvis.redis.ratelimiter;

import lombok.extern.slf4j.Slf4j;
import org.jarvis.clock.Stopwatch;
import org.jarvis.clock.TimeUtils;

import java.util.concurrent.TimeUnit;

/**
 * 基于令牌桶算法实现
 */
@Slf4j
public class SimpleRateLimiter extends RateLimiter {

    private final Object MUTEX = new Object();

    //# how many tokens per second in token-bucket algorithm.
    private final double permitsPerSecond;
    //# how many tokens the bucket can hold in token-bucket
    private final int burstCapacity;

    private double reservedCapacity;

    private long tick;


    public SimpleRateLimiter(double permitsPerSecond) {
        this(permitsPerSecond, ((int) permitsPerSecond));
    }


    public SimpleRateLimiter(double permitsPerSecond, int burstCapacity) {
        this.permitsPerSecond = permitsPerSecond;
        this.burstCapacity = burstCapacity;
        this.reservedCapacity = burstCapacity;
        this.tick = System.currentTimeMillis();
    }

    @Override
    public double acquire(int permits) {
        synchronized (MUTEX) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            boolean allowed;
            do {
                allowed = isAllowed(permits);
            } while (!allowed);
            return stopwatch.elapsedMillis();
        }
    }

    @Override
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        synchronized (MUTEX) {
            boolean allowed;
            do {
                allowed = isAllowed(permits);
            } while (!allowed && stopwatch.elapsedMillis() <= unit.toMillis(timeout));
            return allowed;
        }
    }

    private boolean isAllowed(int permits) {
        boolean allowed;
        long now = System.currentTimeMillis();
        double latestCapacity = Math.min(burstCapacity, reservedCapacity + (now - tick) / 1000d * permitsPerSecond);
        allowed = latestCapacity >= permits;
        this.reservedCapacity = allowed ? latestCapacity - permits : latestCapacity;
        this.tick = now;
        if (!allowed) {
            double duration = (permits - latestCapacity) / permitsPerSecond * 1000000;
            TimeUtils.sleep(((long) duration), TimeUnit.MICROSECONDS);
        }
        return allowed;
    }

    @Override
    public String getName() {
        return null;
    }
}
