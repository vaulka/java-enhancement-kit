package com.pongsky.repository.config.redis;

import com.pongsky.repository.config.SystemConfig;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2021-11-25 14:37
 **/
public class KeyStringRedisSerializer implements RedisSerializer<String> {

    private final Charset UTF_8 = StandardCharsets.UTF_8;

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return bytes == null ? null : new String(bytes, UTF_8).replaceFirst(SystemConfig.getServiceName() + ":", "");
    }

    @Override
    public byte[] serialize(@Nullable String string) {
        return string == null ? null : (SystemConfig.getServiceName() + ":" + string).getBytes(UTF_8);
    }

}