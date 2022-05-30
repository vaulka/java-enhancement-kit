package com.pongsky.kit.trace.config.async;

import com.pongsky.kit.common.trace.TraceInfo;
import com.pongsky.kit.common.trace.TraceThreadLocal;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 异步间传递数据
 *
 * @author pengsenhao
 */
public class RequestContextDecorator implements TaskDecorator {

    @Nonnull
    @Override
    public Runnable decorate(@Nonnull Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        TraceInfo traceInfo = TraceThreadLocal.getTraceInfo();
        return () -> {
            try {
                MDC.setContextMap(contextMap);
                TraceThreadLocal.setTraceInfo(traceInfo);
                runnable.run();
            } finally {
                MDC.clear();
                TraceThreadLocal.delTraceInfo();
            }
        };
    }

}
