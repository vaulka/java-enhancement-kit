package com.pongsky.kit.cache.redis.script;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Redis 令牌桶限流算法
 *
 * @author pengsenhao
 */
@Slf4j
public class RedisRateLimitScript implements RedisScript<String> {

    private final String script;

    public RedisRateLimitScript() {
        InputStream inputStream = RedisRateLimitScript.class.getResourceAsStream("/script/rateLimit.lua");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = null;
        byte[] buffer = new byte[1024];
        int length;
        try {
            if (inputStream != null) {
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
            }
            content = outputStream.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        script = content;
    }

    @NonNull
    @Override
    public String getSha1() {
        return DigestUtils.sha1Hex(script);
    }

    @Override
    public Class<String> getResultType() {
        return String.class;
    }

    @NonNull
    @Override
    public String getScriptAsString() {
        return script;
    }

}