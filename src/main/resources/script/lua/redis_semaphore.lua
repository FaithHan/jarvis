local capacity = tonumber(ARGV[1])
local ttl = tonumber(ARGV[2])
local requests = ARGV[3]
local time = redis.call('time')
local timestamp = time[1] * 1000000 + time[2]

-- Clear out old requests that probably got lost
redis.call('zremrangebyscore', KEYS[1], '-inf', timestamp - ttl)

redis.call('pexpire', KEYS[1], 2 * ttl)

local reserved = redis.call("zcard", KEYS[1])

local balance = capacity - reserved

if balance >= requests then
    for i = 1, requests do
        redis.call("zadd", KEYS[1], timestamp, tostring(timestamp) .. tostring(i))
    end
    return 1
else
    return 0
end

local items = redis.call('zrange', KEYS[1], '-inf', 'inf', 'byscore', 'LIMIT', 0, ARGV[1])
for index, value in pairs(items) do
    redis.call('zrem', KEYS[1], value)
end
