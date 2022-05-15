package com.pongsky.kit.cache.redis.service;

import com.pongsky.kit.cache.redis.properties.RedisCacheProperties;
import com.pongsky.kit.cache.redis.script.RedisRateLimitScript;
import com.pongsky.kit.common.exception.FrequencyException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * 令牌桶限流 service
 *
 * @author pengsenhao
 */
@RequiredArgsConstructor
public class RateLimitService {

    private final RedisCacheProperties properties;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 限流错误返回结果
     */
    private static final String RATE_LIMIT_ERROR_RESULT = "0";

    /**
     * 限流 lua 脚本
     */
    private static final RedisRateLimitScript REDIS_RATE_LIMIT_SCRIPT = new RedisRateLimitScript();

    /**
     * 校验是否要限流
     *
     * @param key  key
     * @param max  令牌桶大小
     * @param rate 令牌每秒恢复速度
     */
    public void validation(String key, int max, int rate) {
        String result = redisTemplate.execute(REDIS_RATE_LIMIT_SCRIPT,
                Collections.singletonList(MessageFormat.format("{0}:{1}", properties.getPrefix(), key)),
                Integer.toString(max),
                Integer.toString(rate),
                Long.toString(System.currentTimeMillis()));
        if (RATE_LIMIT_ERROR_RESULT.equals(result)) {
            throw new FrequencyException("当前请求过于频繁，请稍后再试");
        }
    }

}
