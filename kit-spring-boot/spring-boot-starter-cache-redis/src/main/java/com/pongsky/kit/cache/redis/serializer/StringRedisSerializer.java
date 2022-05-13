package com.pongsky.kit.cache.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * String 序列化 key 配置
 * <p>
 * 增加统一的前缀，适用于多项目用同一个 Redis 库时，通过项目前缀进行区分
 *
 * @author pengsenhao
 **/
public class StringRedisSerializer implements RedisSerializer<String> {

    private final String regex;

    public StringRedisSerializer(String prefix) {
        this.regex = prefix + ":";
    }

    private final Charset UTF_8 = StandardCharsets.UTF_8;

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return bytes == null ? null : new String(bytes, UTF_8).replaceFirst(regex, "");
    }

    @Override
    public byte[] serialize(@Nullable String string) {
        return string == null ? null : (regex + string).getBytes(UTF_8);
    }

}