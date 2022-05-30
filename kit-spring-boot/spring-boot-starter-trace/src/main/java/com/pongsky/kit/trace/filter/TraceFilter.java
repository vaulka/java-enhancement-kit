package com.pongsky.kit.trace.filter;

import com.pongsky.kit.common.trace.TraceConstants;
import com.pongsky.kit.common.trace.TraceInfo;
import com.pongsky.kit.common.trace.TraceThreadLocal;
import com.pongsky.kit.trace.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 链路拦截器
 *
 * @author pengsenhao
 */
@RequiredArgsConstructor
public class TraceFilter implements Filter {

    private final ApplicationProperties properties;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String traceId = TraceThreadLocal.buildTraceId(httpServletRequest);
        // 设置响应头
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(TraceConstants.X_TRACE_ID, traceId);
        httpServletResponse.setHeader(TraceConstants.X_HOSTNAME, properties.getHostName());
        httpServletResponse.setHeader(TraceConstants.X_INSTANCE_ID, properties.getInstanceId());
        httpServletResponse.setHeader(TraceConstants.X_BACKEND_VERSION, properties.getVersion());
        // 设置链路信息
        MDC.put(TraceConstants.X_TRACE_ID, traceId);
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        Map<String, String> requestHeaders = new HashMap<>(16);
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            requestHeaders.put(key, value);
        }
        String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        TraceInfo traceInfo = new TraceInfo()
                .setTraceId(traceId)
                .setAuthorization(authorization)
                .setRequestHeaders(requestHeaders);
        TraceThreadLocal.setTraceInfo(traceInfo);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
            TraceThreadLocal.delTraceInfo();
        }
    }

}
