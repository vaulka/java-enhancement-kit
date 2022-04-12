package com.pongsky.kit.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.config.SystemConfig;
import com.pongsky.kit.ip.utils.IpUtils;
import com.pongsky.kit.response.GlobalResult;
import com.pongsky.kit.response.annotation.ResponseResult;
import com.pongsky.kit.utils.trace.CurrentThreadConfig;
import com.pongsky.kit.utils.trace.DiyHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 接口响应体处理器
 *
 * @author pengsenhao
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnMissingBean(ResponseBodyAdvice.class)
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper jsonMapper;
    private final SystemConfig systemConfig;

    @Override
    public boolean supports(@Nonnull MethodParameter returnType,
                            @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @Nonnull MethodParameter returnType,
                                  @Nonnull MediaType selectedContentType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nonnull ServerHttpRequest request,
                                  @Nonnull ServerHttpResponse response) {
        String traceId = CurrentThreadConfig.getTraceId();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        Optional.ofNullable(attributes.getResponse())
                .ifPresent(httpServletResponse -> {
                    httpServletResponse.setHeader(DiyHeader.X_HOSTNAME, systemConfig.getHostName());
                    httpServletResponse.setHeader(DiyHeader.X_INSTANCE_ID, systemConfig.getInstanceId());
                    httpServletResponse.setHeader(DiyHeader.X_TRACE_ID, traceId);
                    httpServletResponse.setHeader(DiyHeader.X_BACKEND_VERSION, systemConfig.getVersion());
                });
        HttpServletRequest httpServletRequest = attributes.getRequest();
        // 判断是否已封装好全局响应结果
        if (body instanceof GlobalResult) {
            GlobalResult<?> result = (GlobalResult<?>) body;
            if (result.getIp() == null) {
                result.setIp(IpUtils.getIp(httpServletRequest));
            }
            if (result.getPath() == null) {
                result.setPath(httpServletRequest.getRequestURI());
            }
            return result;
        }
        // 特殊业务场景返回特定格式 直接 return
        // if (httpServletRequest.getAttribute(XXX.class.getSimpleName()) != null) {
        // return body;
        // }
        // 判断是否全局响应数据
        if (httpServletRequest.getAttribute(ResponseResult.class.getSimpleName()) != null) {
            if (body instanceof String) {
                try {
                    return jsonMapper.writeValueAsString(GlobalResult.success(body));
                } catch (JsonProcessingException e) {
                    log.error(e.getLocalizedMessage());
                    return GlobalResult.success(body);
                }
            } else {
                return GlobalResult.success(body);
            }
        }
        return body;
    }

}