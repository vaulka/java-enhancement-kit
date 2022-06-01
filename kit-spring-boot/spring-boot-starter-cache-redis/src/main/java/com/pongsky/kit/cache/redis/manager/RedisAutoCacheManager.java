package com.pongsky.kit.cache.redis.manager;

import lombok.NonNull;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 配置 {@link Cacheable} 缓存时间
 * <p>
 * 使用 # 分隔，支持 ms（毫秒），s（秒默认），m（分），h（小时），d（天）等单位。
 * <p>
 * 示例： @Cacheable(cacheNames = "user#5m", key = "#id")
 *
 * @author pengsenhao
 */
public class RedisAutoCacheManager extends RedisCacheManager {

    public RedisAutoCacheManager(RedisCacheWriter cacheWriter,
                                 RedisCacheConfiguration defaultCacheConfiguration,
                                 Map<String, RedisCacheConfiguration> initialCacheConfigurations,
                                 boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    /**
     * 井号
     */
    private static final String HASHTAG = "#";

    /**
     * 间隔数量
     */
    private static final int SPLIT_LENGTH = 2;

    @NonNull
    @Override
    protected RedisCache createRedisCache(@NonNull String name, RedisCacheConfiguration cacheConfig) {
        String[] caches = name.split(HASHTAG);
        if (SPLIT_LENGTH != caches.length) {
            return super.createRedisCache(name, cacheConfig);
        }
        String key = caches[0];
        String time = caches[1];
        if (cacheConfig != null) {
            // 转换时间，支持时间单位例如：300ms，第二个参数是默认单位
            Duration duration = DurationStyle.detectAndParse(time, ChronoUnit.SECONDS);
            cacheConfig = cacheConfig.entryTtl(duration);
        }
        return super.createRedisCache(key, cacheConfig);
    }

}
