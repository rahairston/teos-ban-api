package com.teosgame.ban.banapi.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisClient {
    @Qualifier("redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    private final Integer TIMEOUT = 30;

    Logger logger = LoggerFactory.getLogger(RedisClient.class);

    public void setValue(final String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, TIMEOUT, TimeUnit.MINUTES);
    }

    public String getValue(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
