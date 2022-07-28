package org.jarvis.redis.distributedlock;

import java.util.concurrent.TimeUnit;

public class RedisReadWriteLock {


    public void lock() {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {

    }

}
