-- function
local getTimestamp = function()
    local temp = redis.call('time')
    return temp[1] + temp[2] / 1000000
end

local key = KEYS[1]

local rate = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local requested = tonumber(ARGV[3])
local ttl = tonumber(ARGV[4])
local now = getTimestamp()

local info = redis.call("hgetall", key)

if (#info == 0) then
    redis.call('hset', key, 'permits', capacity - requested, 'timestamp', now)
    redis.call('expire', key, ttl)
    return { 1, 0 }
end

local last_tokens = info[2]
local last_timestamp = tonumber(info[4])
local delta = math.max(0, now - last_timestamp)
local filled_tokens = math.min(capacity, last_tokens + delta * rate)
local new_tokens = filled_tokens
local allowed = filled_tokens >= requested

if allowed then
    new_tokens = filled_tokens - requested
end

local new_timestamp = now

if new_timestamp < last_timestamp then
    new_timestamp = last_timestamp
end

redis.call('hset', key, 'permits', new_tokens, 'timestamp', new_timestamp)
redis.call('expire', key, ttl)

local sleepTime = 0
if (not allowed) then
    sleepTime = math.max(0, (requested - new_tokens) / rate * 1000 - 5)
end
if allowed then
    return { 1, sleepTime }
end
return { 0, sleepTime }