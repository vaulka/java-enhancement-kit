package com.pongsky.springcloud.config.redis;

import com.pongsky.springcloud.config.SystemConfig;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 序列化 String key 配置
 *
 * @author pengsenhao
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