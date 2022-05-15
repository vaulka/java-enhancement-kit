package com.pongsky.kit.cache.redis.script;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * Redis 令牌桶限流算法
 *
 * @author pengsenhao
 */
public class RedisRateLimitScript implements RedisScript<String> {

    private static final String SCRIPT =
            "local ratelimit_info = redis.pcall('HMGET',KEYS[1],'last_time','current_token') " +
                    " " +
                    "local last_time = ratelimit_info[1] " +
                    " " +
                    "local current_token = tonumber(ratelimit_info[2]) " +
                    " " +
                    "local max_token = tonumber(ARGV[1]) " +
                    " " +
                    "local token_rate = tonumber(ARGV[2]) " +
                    " " +
                    "local current_time = tonumber(ARGV[3]) " +
                    " " +
                    "local reverse_time = 1000/token_rate " +
                    " " +
                    "if current_token == nil then " +
                    " " +
                    "  current_token = max_token " +
                    " " +
                    "  last_time = current_time " +
                    " " +
                    "else " +
                    " " +
                    "  local past_time = current_time-last_time " +
                    " " +
                    "  local reverse_token = math.floor(past_time/reverse_time) " +
                    " " +
                    "  current_token = current_token+reverse_token " +
                    " " +
                    "  last_time = reverse_time*reverse_token+last_time " +
                    " " +
                    "  if current_token>max_token then " +
                    " " +
                    "    current_token = max_token " +
                    " " +
                    "  end " +
                    " " +
                    "end " +
                    " " +
                    "local result = '0' " +
                    " " +
                    "if(current_token>0) then " +
                    " " +
                    "  result = '1' " +
                    " " +
                    "  current_token = current_token-1 " +
                    " " +
                    "end  " +
                    " " +
                    "redis.call('HMSET',KEYS[1],'last_time',last_time,'current_token',current_token) " +
                    " " +
                    "redis.call('pexpire',KEYS[1],math.ceil(reverse_time*(max_token-current_token)+(current_time-last_time))) " +
                    " " +
                    "return result";

    @Override
    public String getSha1() {
        return DigestUtils.sha1Hex(SCRIPT);
    }

    @Override
    public Class<String> getResultType() {
        return String.class;
    }

    @Override
    public String getScriptAsString() {
        return SCRIPT;
    }

}