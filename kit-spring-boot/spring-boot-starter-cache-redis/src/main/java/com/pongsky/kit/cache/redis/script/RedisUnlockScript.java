package com.pongsky.kit.cache.redis.script;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * Redis 锁
 *
 * @author pengsenhao
 */
public class RedisUnlockScript implements RedisScript<Long> {

    private static final String SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return -1 end";

    @Override
    public String getSha1() {
        return DigestUtils.sha1Hex(SCRIPT);
    }

    @Override
    public Class<Long> getResultType() {
        return Long.class;
    }

    @Override
    public String getScriptAsString() {
        return SCRIPT;
    }

}