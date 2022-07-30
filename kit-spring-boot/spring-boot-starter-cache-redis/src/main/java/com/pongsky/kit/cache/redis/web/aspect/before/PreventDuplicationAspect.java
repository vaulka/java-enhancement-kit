package com.pongsky.kit.cache.redis.web.aspect.before;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.cache.redis.annotation.PreventDuplication;
import com.pongsky.kit.cache.redis.handler.PreventDuplicationHandler;
import com.pongsky.kit.common.exception.FrequencyException;
import com.pongsky.kit.common.utils.HttpServletRequestUtils;
import com.pongsky.kit.common.utils.IpUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 防重检测
 *
 * @author pengsenhao
 */
@Aspect
@ConditionalOnProperty(name = "spring.cache.prevent-duplication.enabled", havingValue = "true", matchIfMissing = true)
public class PreventDuplicationAspect {

    private final ObjectMapper jsonMapper;
    private final PreventDuplicationHandler handler;
    private final RedisTemplate<String, Object> redisTemplate;

    public PreventDuplicationAspect(ObjectMapper jsonMapper,
                                    RedisTemplate<String, Object> redisTemplate,
                                    ApplicationContext applicationContext) {
        this.jsonMapper = jsonMapper;
        this.redisTemplate = redisTemplate;
        this.handler = applicationContext.getBeansOfType(PreventDuplicationHandler.class).values().stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * 防重 key
     * <p>
     * ip:sign
     */
    private static final String PREVENT_DUPLICATION_KEY = "prevent-duplication:{0}:{1}";

    @Before("(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) ")
    public void exec(JoinPoint point) throws JsonProcessingException {
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
        // 先在方法上寻找该注解
        PreventDuplication preventDuplication = Optional.ofNullable(AnnotationUtils.findAnnotation(method, PreventDuplication.class))
                // 方法上没有的话，则再类上寻找该注解
                .orElse(AnnotationUtils.findAnnotation(signature.getDeclaringType(), PreventDuplication.class));
        // 没有设置防重频率则默认为 100ms 间隔
        int frequency = preventDuplication != null ? preventDuplication.frequency() : PreventDuplication.DEFAULT_FREQUENCY;
        TimeUnit unit = preventDuplication != null ? preventDuplication.unit() : PreventDuplication.DEFAULT_UNIT;
        String ip = IpUtils.getIp(request);
        RequestInfo requestInfo = new RequestInfo()
                .setUri(request.getRequestURI())
                .setMethod(request.getMethod())
                .setQuery(Optional.ofNullable(request.getQueryString()).orElse(""))
                .setRequestBody(Optional.ofNullable(HttpServletRequestUtils.getBody(request)).orElse(""));
        String sign = DigestUtils.sha1DigestAsHex(jsonMapper.writeValueAsString(requestInfo));
        String key = MessageFormat.format(PREVENT_DUPLICATION_KEY, ip, sign);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new FrequencyException("当前重复请求过于频繁，请稍后再试");
        }
        redisTemplate.opsForValue().set(key, "", frequency, unit);
    }

    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = false)
    public static class RequestInfo {

        /**
         * uri
         */
        private String uri;

        /**
         * method
         */
        private String method;

        /**
         * query
         */
        private String query;

        /**
         * requestBody
         */
        private String requestBody;

    }

}
