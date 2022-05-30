package com.pongsky.kit.trace.config.feign;

import com.pongsky.kit.common.trace.TraceConstants;
import com.pongsky.kit.common.trace.TraceThreadLocal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpHeaders;

/**
 * Feign 配置
 *
 * @author pengsenhao
 */
@ConditionalOnClass(RequestInterceptor.class)
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 重新设置 user-agent，原始 user-agent 是 jdk 版本，会变化
        requestTemplate.header(HttpHeaders.USER_AGENT, TraceConstants.REMOTE_PREFIX);
        requestTemplate.header(HttpHeaders.AUTHORIZATION, TraceThreadLocal.getAuthorization());
        String traceId = TraceThreadLocal.getTraceId();
        if (StringUtils.isNotBlank(traceId)) {
            requestTemplate.header(TraceConstants.X_TRACE_ID, traceId.contains(TraceConstants.REMOTE_PREFIX)
                    ? traceId
                    : traceId.replace(TraceConstants.ORIGIN_PREFIX, TraceConstants.REMOTE_PREFIX));
        }
    }

}