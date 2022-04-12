package com.pongsky.kit.web.aspect.around;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.ip.utils.IpUtils;
import com.pongsky.kit.web.request.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * controller 请求日志打印
 *
 * @author pengsenhao
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RestControllerAspect {

    private final ObjectMapper jsonMapper;

    @Around("(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) ")
    public Object exec(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String ip = IpUtils.getIp(request);
        String userAgent = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT)).orElse("");
        String referer = Optional.ofNullable(request.getHeader(HttpHeaders.REFERER)).orElse("");
        RequestMethod requestMethod = StringUtils.isBlank(request.getMethod())
                ? RequestMethod.GET
                : RequestMethod.valueOf(request.getMethod());
        switch (requestMethod) {
            case GET:
            case PUT:
            case POST:
            case DELETE:
                log.info("");
                log.info("Started request");
                log.info("request header: IP [{}] userAgent [{}] referer [{}] authorization [{}]",
                        ip, userAgent, referer, authorization);
                log.info("request desc: uri [{}] method [{}] params [{}] body [{}]",
                        request.getRequestURI(),
                        request.getMethod(),
                        Optional.ofNullable(request.getQueryString()).orElse(""),
                        Optional.ofNullable(RequestUtils.getBody(request)).orElse(""));
                log.info("response: [{}]", jsonMapper.writeValueAsString(Optional.ofNullable(result).orElse("")));
                log.info("cost [{}] ms", System.currentTimeMillis() - start);
                log.info("Ended request");
                break;
            default:
                break;
        }
        return result;
    }

}
