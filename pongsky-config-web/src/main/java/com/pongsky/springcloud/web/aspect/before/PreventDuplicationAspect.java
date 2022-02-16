package com.pongsky.springcloud.web.aspect.before;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.springcloud.exception.FrequencyException;
import com.pongsky.springcloud.utils.IpUtils;
import com.pongsky.springcloud.utils.model.annotation.PreventDuplication;
import com.pongsky.springcloud.utils.trace.DiyHeader;
import com.pongsky.springcloud.web.request.RequestUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 防重检测
 *
 * @author pengsenhao
 */
@Aspect
@Component
@Order(value = 2)
@RequiredArgsConstructor
public class PreventDuplicationAspect {

    private final ObjectMapper jsonMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 防重 key
     * <p>
     * ip:sign
     */
    private static final String PREVENT_DUPLICATION = "PREVENT-DUPLICATION:{0}:{1}";

    /**
     * 获取 防重 key
     *
     * @param ip   ip
     * @param sign 签名
     * @return 获取 防重 key
     */
    private String getPreventDuplicationKey(String ip, String sign) {
        return MessageFormat.format(PREVENT_DUPLICATION, ip, sign);
    }

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
        String userAgent = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT)).orElse("");
        String ip = IpUtils.getIp(request);
        if (DiyHeader.REMOTE_PREFIX.equals(userAgent)) {
            // feign 远程调用则不统计
            return;
        }
        PreventDuplication preventDuplication = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(PreventDuplication.class);
        int frequency = preventDuplication != null ? preventDuplication.frequency() : PreventDuplication.DEFAULT_FREQUENCY;
        RequestInfo requestInfo = new RequestInfo()
                .setUri(request.getRequestURI())
                .setMethod(request.getMethod())
                .setQuery(Optional.ofNullable(request.getQueryString()).orElse(""))
                .setRequestBody(Optional.ofNullable(RequestUtils.getBody(request)).orElse(""));
        String sign = DigestUtils.sha1DigestAsHex(jsonMapper.writeValueAsString(requestInfo));
        String key = this.getPreventDuplicationKey(ip, sign);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new FrequencyException("当前请求过于频繁，请稍后再试");
        }
        redisTemplate.opsForValue().set(key, "", frequency, TimeUnit.MILLISECONDS);
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
