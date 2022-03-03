package com.pongsky.kit.web.filter;

import com.pongsky.kit.utils.trace.CurrentInfo;
import com.pongsky.kit.utils.trace.CurrentThreadConfig;
import com.pongsky.kit.utils.trace.DiyHeader;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 链路拦截器
 *
 * @author pengsenhao
 */
@Slf4j
@Configuration
public class TraceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String traceId = CurrentThreadConfig.buildTraceId(httpServletRequest);
        MDC.put(DiyHeader.X_TRACE_ID, traceId);
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        Map<String, String> requestHeaders = new HashMap<>(16);
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            requestHeaders.put(key, value);
        }
        String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        CurrentInfo currentInfo = new CurrentInfo()
                .setTraceId(traceId)
                .setAuthorization(authorization)
                .setRequestHeaders(requestHeaders);
        CurrentThreadConfig.setCurrentInfo(currentInfo);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
            CurrentThreadConfig.relCurrentInfo();
        }
    }

    @Override
    public void destroy() {
    }

}
