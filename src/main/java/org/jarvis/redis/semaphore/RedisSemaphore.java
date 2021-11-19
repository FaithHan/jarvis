package org.jarvis.redis.semaphore;

import lombok.extern.slf4j.Slf4j;
import org.jarvis.clock.Stopwatch;
import org.jarvis.clock.TimeUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("all")
public class RedisSemaphore {

    private static final String PREFIX = "jarvis_semaphore@";

    private String name;

    private StringRedisTemplate stringRedisTemplate;

    private final int threshold;

    private final List<String> keys;

    private final long ttl;

    private static final int RETRY_INTERVAL = 50;

    public RedisSemaphore(String name, int threshold, StringRedisTemplate stringRedisTemplate) {
        this(name, threshold, 15 * 1000, stringRedisTemplate);
    }

    public RedisSemaphore(String name, int threshold, long ttl, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.threshold = threshold;
        this.ttl = ttl * 1000;
        this.keys = Collections.singletonList(PREFIX + name);
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void acquire() {
        acquire(1);
    }

    public synchronized void acquire(int permits) {
        boolean result = stringRedisTemplate.execute(ACQUIRE_SCRIPT, keys, String.valueOf(threshold), String.valueOf(ttl), String.valueOf(permits)) > 0;
        while (!result) {
            TimeUtils.sleep(RETRY_INTERVAL, TimeUnit.MILLISECONDS);
            result = stringRedisTemplate.execute(ACQUIRE_SCRIPT, keys, String.valueOf(threshold), String.valueOf(ttl), String.valueOf(permits)) > 0;
        }
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0, TimeUnit.MILLISECONDS);
    }

    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        boolean result = stringRedisTemplate.execute(ACQUIRE_SCRIPT, keys, String.valueOf(threshold), String.valueOf(ttl), String.valueOf(permits)) > 0;
        while (!result && stopwatch.elapsedMillis() < unit.toMillis(timeout)) {
            TimeUtils.sleep(RETRY_INTERVAL, TimeUnit.MILLISECONDS);
            result = stringRedisTemplate.execute(ACQUIRE_SCRIPT, keys, String.valueOf(threshold), String.valueOf(ttl), String.valueOf(permits)) > 0;
        }
        return result;
    }

    public void release() {
        release(1);
    }

    public void release(int permits) {
        stringRedisTemplate.execute(RELEASE_SCRIPT, keys, String.valueOf(permits));
    }

    private static final DefaultRedisScript<Long> ACQUIRE_SCRIPT = new DefaultRedisScript<Long>() {
        {
            setResultType(Long.class);
            setScriptText(
                    "local capacity = tonumber(ARGV[1])\n" +
                    "local ttl = tonumber(ARGV[2])\n" +
                    "local requests = tonumber(ARGV[3])\n" +
                    "local time = redis.call('time')\n" +
                    "local timestamp = time[1] * 1000000 + time[2]\n" +
                    "\n" +
                    "-- Clear out old requests that probably got lost\n" +
                    "redis.call('zremrangebyscore', KEYS[1], '-inf', timestamp - ttl)\n" +
                    "\n" +
                    "redis.call('pexpire', KEYS[1], 2 * ttl)\n" +
                    "\n" +
                    "local reserved = redis.call(\"zcard\", KEYS[1])\n" +
                    "\n" +
                    "local balance = capacity - reserved\n" +
                    "\n" +
                    "if balance >= requests then\n" +
                    "    for i = 1, requests do\n" +
                    "        redis.call(\"zadd\", KEYS[1], timestamp, string.format('%d',timestamp) .. i)\n" +
                    "    end\n" +
                    "    return 1\n" +
                    "else\n" +
                    "    return 0\n" +
                    "end");
        }
    };

    private static final DefaultRedisScript<Void> RELEASE_SCRIPT = new DefaultRedisScript<Void>() {
        {
            setResultType(Void.class);
            setScriptText(
                    "local items = redis.call('zrange', KEYS[1], '-inf', 'inf', 'byscore', 'LIMIT', 0, ARGV[1])\n" +
                    "for index, value in ipairs(items) do\n" +
                    "    redis.call('zrem', KEYS[1], value)\n" +
                    "end");
        }
    };

}
