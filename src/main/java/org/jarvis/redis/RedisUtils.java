package org.jarvis.redis;

import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Instant;

public class RedisUtils {

    public static StringRedisTemplate getStringRedisTemplate(int port) {
        return getStringRedisTemplate("127.0.0.1", port);
    }

    public static StringRedisTemplate getStringRedisTemplate(String host, int port) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        LettuceConnectionFactory fac = new LettuceConnectionFactory(host, port);
        fac.afterPropertiesSet();

        stringRedisTemplate.setConnectionFactory(fac);
        stringRedisTemplate.afterPropertiesSet();
        long l = Instant.now().toEpochMilli();
        stringRedisTemplate.execute((RedisCallback<Object>) RedisConnectionCommands::ping);
//        System.out.println(Instant.now().toEpochMilli() - l);

        l = Instant.now().toEpochMilli();
        stringRedisTemplate.execute((RedisCallback<Object>) RedisConnectionCommands::ping);
//        System.out.println(Instant.now().toEpochMilli() - l);
        return stringRedisTemplate;
    }
}
