package com.pongsky.kit.config.redis;

import com.pongsky.kit.config.SystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 序列化 String key 配置
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
public class KeyStringRedisSerializer implements RedisSerializer<String> {

    private final SystemConfig systemConfig;

    private final Charset UTF_8 = StandardCharsets.UTF_8;

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return bytes == null ? null : new String(bytes, UTF_8).replaceFirst(systemConfig.getServiceName() + ":", "");
    }

    @Override
    public byte[] serialize(@Nullable String string) {
        return string == null ? null : (systemConfig.getServiceName() + ":" + string).getBytes(UTF_8);
    }

}