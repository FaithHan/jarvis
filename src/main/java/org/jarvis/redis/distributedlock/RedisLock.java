package org.jarvis.redis.distributedlock;

import lombok.extern.slf4j.Slf4j;
import org.jarvis.clock.Stopwatch;
import org.jarvis.clock.TimeUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("all")
public class RedisLock {


    private static final String ID = UUID.randomUUID().toString().replaceAll("-", "");

    private static final String PREFIX = "jarvis_lock@";

    private static final int DEFALUT_RETRY_INTERVAL = 50;

    private final StringRedisTemplate stringRedisTemplate;

    private final String name;

    private final List<String> keys;

    private final long ttl;

    private final int retryInterval;

    private final long watchDogTime = 8000;

    private volatile WatchDog watchDog;

    public RedisLock(StringRedisTemplate stringRedisTemplate, String name) {
        this(stringRedisTemplate, name, 3000, DEFALUT_RETRY_INTERVAL);
    }


    public RedisLock(StringRedisTemplate stringRedisTemplate, String name, long ttl, int retryInterval) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.name = name;
        this.ttl = ttl;
        this.retryInterval = retryInterval;
        this.keys = Collections.singletonList(PREFIX + name);
    }

    public void lock() {
        while (!tryLock()) {
            TimeUtils.sleep(retryInterval, TimeUnit.MILLISECONDS);
        }
    }

    public boolean tryLock() {
        String holder = getHolder();
        Long result = stringRedisTemplate.execute(LOCK_SCRIPT, keys, holder, String.valueOf(ttl));
        if (result > 0 && this.watchDog == null) {
            addWatchDog(holder);
        }
        return result > 0;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        long timeout = unit.toMillis(time);
        boolean result = tryLock();
        while (!result && stopwatch.elapsedMillis() < timeout) {
            result = tryLock();
        }
        return result;
    }

    public void unlock() {
        String holder = getHolder();
        long times = stringRedisTemplate.execute(UNLOCK_SCRIPT, keys, holder);
        if (times == 0) {
            removeWatchDog();
        } else if (times < 0) {
            throw new RuntimeException("未持有锁不能释放");
        }
    }

    private String getHolder() {
        return ID + "_" + Thread.currentThread().getId();
    }

    private void addWatchDog(String value) {
        this.watchDog = new WatchDog(value);
    }

    private void removeWatchDog() {
        if (watchDog != null && watchDog.isHolder(getHolder())) {
            this.watchDog.stop();
            this.watchDog = null;
        }
    }


    private class WatchDog {

        private final String holder;

        private final Thread task;

        private volatile int count = 0;

        public WatchDog(String holder) {
            this.holder = holder;
            this.task = new Thread(() -> {
                int count = 0;
                while (true) {
                    count++;
                    try {
                        long sleepTime = count == 1 ? 2 * ttl / 3 : 2 * watchDogTime / 3;
                        Thread.sleep(sleepTime);
                        stringRedisTemplate.execute(WATCH_DOG_SCRIPT, keys, holder, String.valueOf(watchDogTime));
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            this.task.start();
        }

        boolean isHolder(String holder) {
            return this.holder.equals(holder);
        }

        public void stop() {
            task.interrupt();
        }
    }

    private static final DefaultRedisScript<Long> LOCK_SCRIPT = new DefaultRedisScript<Long>() {
        {
            setResultType(Long.class);
            setScriptText(
                    "local key = KEYS[1]\n" +
                    "local value = ARGV[1]\n" +
                    "local ttl = tonumber(ARGV[2])\n" +
                    "\n" +
                    "if (redis.call('exists', key) == 1) then\n" +
                    "    if (redis.call('hget', key, 'locker') == value) then\n" +
                    "        --如果已经持有锁, 重入\n" +
                    "        return redis.call('hincrby', key, 'times', 1)\n" +
                    "    else\n" +
                    "        -- 如果未持有, 则抢占锁未成功\n" +
                    "        return 0\n" +
                    "    end\n" +
                    "else\n" +
                    "    redis.call('hset', key, 'locker', value)\n" +
                    "    local times = redis.call('hincrby', key, 'times', 1)\n" +
                    "    redis.call('pexpire', key, ttl)\n" +
                    "    return times\n" +
                    "end");
        }
    };

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<Long>() {
        {
            setResultType(Long.class);
            setScriptText(
                    "if (redis.call('exists', KEYS[1]) == 1 and redis.call('hget', KEYS[1], 'locker') == ARGV[1]) then\n" +
                    "    local times = redis.call('hincrby', KEYS[1], 'times', -1)\n" +
                    "    if (times == 0) then\n" +
                    "        redis.call('del', KEYS[1])\n" +
                    "    end\n" +
                    "    return times\n" +
                    "else\n" +
                    "    return -1\n" +
                    "end");
        }
    };

    private static final DefaultRedisScript<Long> WATCH_DOG_SCRIPT = new DefaultRedisScript<Long>() {
        {
            setResultType(Long.class);
            setScriptText(
                    "if (redis.call('exists', KEYS[1]) == 1 and redis.call('hget', KEYS[1], 'locker') == ARGV[1]) then\n" +
                    "    redis.call('pexpire', KEYS[1], ARGV[2])\n" +
                    "end");
        }
    };

}
