package org.jarvis.redis.ratelimiter;

import lombok.extern.slf4j.Slf4j;
import org.jarvis.clock.Stopwatch;
import org.jarvis.clock.TimeUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基于令牌桶算法实现
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class RedisRateLimiter extends RateLimiter {

    private static final String DEFAULT_NAME = "redis_rate_limiter";

    private static final String SCRIPT_PATH = "script/lua/redis_rate_limiter.lua";

    private static final DefaultRedisScript<List> LIMIT_REDIS_SCRIPT;

    static {
        LIMIT_REDIS_SCRIPT = new DefaultRedisScript<>();
        LIMIT_REDIS_SCRIPT.setScriptSource(new ResourceScriptSource(new ClassPathResource(SCRIPT_PATH)));
        LIMIT_REDIS_SCRIPT.setResultType(List.class);
    }

    private final String name;

    private final List<String> keys;
    //# how many tokens per second in token-bucket algorithm.
    private final String permitsPerSecond;
    //# how many tokens the bucket can hold in token-bucket
    private final String burstCapacity;

    private final String ttl;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisRateLimiter(double permitsPerSecond, int burstCapacity, StringRedisTemplate stringRedisTemplate) {
        this(permitsPerSecond, burstCapacity, stringRedisTemplate, DEFAULT_NAME);
    }

    public RedisRateLimiter(double permitsPerSecond, int burstCapacity, StringRedisTemplate stringRedisTemplate, String name) {
        this.permitsPerSecond = String.valueOf(permitsPerSecond);
        this.burstCapacity = String.valueOf(burstCapacity);
        this.stringRedisTemplate = stringRedisTemplate;
        this.name = name;
        this.ttl = String.valueOf(Math.ceil(burstCapacity / permitsPerSecond));
        this.keys = Collections.singletonList(name);
    }

    @Override
    @SuppressWarnings("all")
    public double acquire(int permits) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String replenishRate = this.permitsPerSecond;
        String burstCapacity = this.burstCapacity;
        String requested = String.valueOf(permits);

        boolean allowed = false;
        long sleepTime = 0;
        while (!allowed) {
            TimeUtils.sleep(sleepTime, TimeUnit.MILLISECONDS);
            List result = stringRedisTemplate.execute(LIMIT_REDIS_SCRIPT, keys, replenishRate, burstCapacity, requested, ttl);
            allowed = ((Long) result.get(0)) == 1;
            sleepTime = ((Long) result.get(1)).longValue();
        }
        log.info("分布式限流-令牌桶算法, name={}, replenishRate={}, burstCapacity={} 操作执行结果={}", getName(), replenishRate, burstCapacity, allowed);
        return stopwatch.elapsedMillis();
    }

    @Override
    @SuppressWarnings("all")
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        String replenishRate = this.permitsPerSecond;
        String burstCapacity = this.burstCapacity;
        String requested = String.valueOf(permits);

        boolean allowed = false;
        long sleepTime = 0;
        do {
            TimeUtils.sleep(sleepTime, TimeUnit.MILLISECONDS);
            List result = stringRedisTemplate.execute(LIMIT_REDIS_SCRIPT, keys, replenishRate, burstCapacity, requested, ttl);
            allowed = ((long) result.get(0)) == 1;
            sleepTime = ((long) result.get(1));
        } while (!allowed && stopwatch.elapsedMillis() + sleepTime <= unit.toMillis(timeout));

        log.info("分布式限流-令牌桶算法, name={}, replenishRate={}, burstCapacity={} 操作执行结果={}", getName(), replenishRate, burstCapacity, allowed);
        return allowed;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
