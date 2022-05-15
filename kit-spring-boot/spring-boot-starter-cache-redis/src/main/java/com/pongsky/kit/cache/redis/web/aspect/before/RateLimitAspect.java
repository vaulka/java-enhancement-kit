package com.pongsky.kit.cache.redis.web.aspect.before;

import com.pongsky.kit.cache.redis.handler.RateLimitHandler;
import com.pongsky.kit.cache.redis.properties.RateLimitProperties;
import com.pongsky.kit.cache.redis.service.RateLimitService;
import com.pongsky.kit.ip.utils.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * 令牌桶限流
 *
 * @author pengsenhao
 */
@Aspect
@ConditionalOnProperty(name = "spring.cache.rate-limit.enabled", havingValue = "true", matchIfMissing = true)
public class RateLimitAspect {

    private final RateLimitHandler handler;
    private final RateLimitProperties properties;
    private final RateLimitService rateLimitService;

    public RateLimitAspect(RateLimitService rateLimitService,
                           RateLimitProperties properties,
                           ApplicationContext applicationContext) {
        this.rateLimitService = rateLimitService;
        this.properties = properties;
        this.handler = applicationContext.getBeansOfType(RateLimitHandler.class).values().stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * 默认限流 key
     * <p>
     * ip:method:uri
     */
    private static final String DEFAULT_RATE_LIMIT_KEY = "RATE-LIMIT:{0}:{1}:{2}";

    @Before("(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) ")
    public void exec(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        // 判断是否放行
        boolean result = handler != null && handler.release(request, signature, method);
        if (result) {
            return;
        }
        String key = handler != null
                ? handler.getKey(request, signature, method)
                : MessageFormat.format(DEFAULT_RATE_LIMIT_KEY, IpUtils.getIp(request), request.getMethod(), request.getRequestURI());
        int rate = handler != null
                ? handler.getBucketRate(request, signature, method)
                : properties.getBucketRate();
        int max = handler != null
                ? handler.getBucketMax(request, signature, method)
                : properties.getBucketMax();
        rateLimitService.validation(key, max, rate);
    }

}
