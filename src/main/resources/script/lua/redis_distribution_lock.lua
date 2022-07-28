local key = KEYS[1]
local value = ARGV[1]
local ttl = tonumber(ARGV[2])

if (redis.call('exists', key) == 1) then
    if (redis.call('hget', key, 'locker') == value) then
        --如果已经持有锁, 重入
        return redis.call('hincrby', key, 'times', 1)
    else
        -- 如果未持有, 则抢占锁未成功
        return 0
    end
else
    redis.call('hset', key, 'locker', value)
    local times = redis.call('hincrby', key, 'times', 1)
    redis.call('pexpire', key, ttl)
    return times
end

-- unlock
if (redis.call('exists', KEYS[1]) == 1 and redis.call('hget', KEYS[1], 'locker') == ARGV[1]) then
    local times = redis.call('hdecrby', KEYS[1], 'times', -1) == 0
    if (times == 0) then
        redis.call('del', KEYS[1])
    end
    return times
else
    return -1
end

if (redis.call('exists', KEYS[1]) == 1 and redis.call('hget', KEYS[1], 'locker') == ARGV[1]) then
    local reserved = redis.call('ttl', KEYS[1])
    redis.call('pexpire', key, reserved + ARGV[2])
end