package com.pongsky.kit.web.core.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.ip.utils.IpUtils;
import com.pongsky.kit.web.core.utils.HttpServletRequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Controller 请求日志打印
 *
 * @author pengsenhao
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class ControllerAspect {

    private final ObjectMapper jsonMapper;

    /**
     * Controller 切入点
     */
    @Pointcut("(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) ")
    public void point() {
    }

    /**
     * Controller 请求日志打印
     */
    @Before("com.pongsky.kit.web.core.aspect.ControllerAspect.point()")
    public void exec() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = IpUtils.getIp(request);
        String userAgent = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT)).orElse("");
        String referer = Optional.ofNullable(request.getHeader(HttpHeaders.REFERER)).orElse("");
        log.info("");
        log.info("========================= Started REQUEST =========================");
        log.info("REQUEST IP: [{}]", ip);
        log.info("REQUEST userAgent: [{}]", userAgent);
        log.info("REQUEST referer: [{}]", referer);
        log.info("REQUEST uri: [{}]", request.getRequestURI());
        log.info("REQUEST method: [{}]", request.getMethod());
        log.info("REQUEST params: [{}]", Optional.ofNullable(request.getQueryString()).orElse(""));
        log.info("REQUEST body: [{}]", Optional.ofNullable(HttpServletRequestUtils.getBody(request)).orElse(""));
    }

    /**
     * Controller 请求日志打印
     *
     * @param point point
     * @return 响应数据
     * @throws Throwable Throwable
     */
    @Around("com.pongsky.kit.web.core.aspect.ControllerAspect.point()")
    public Object exec(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }
        long start = System.currentTimeMillis();
        try {
            return point.proceed();
        } finally {
            log.info("REQUEST cost: [{}] ms", System.currentTimeMillis() - start);
            log.info("========================== Ended REQUEST ==========================");
        }
    }

    /**
     * Controller 请求日志打印
     *
     * @param result 响应结果
     */
    @AfterReturning(value = "com.pongsky.kit.web.core.aspect.ControllerAspect.point()", returning = "result")
    public void exec(Object result) {
        try {
            log.info("SUCCESS RESPONSE: [{}]", jsonMapper.writeValueAsString(Optional.ofNullable(result).orElse("")));
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            log.info("SUCCESS RESPONSE: [{}]", Optional.ofNullable(result).orElse(""));
        }
    }

    /**
     * Controller 请求日志打印
     *
     * @param exception 异常
     */
    @AfterThrowing(value = "com.pongsky.kit.web.core.aspect.ControllerAspect.point()", throwing = "exception")
    public void exec(Throwable exception) {
        log.error("FAIL RESPONSE: [{}]", Optional.ofNullable(exception.getLocalizedMessage()).orElse(""));
    }

}
