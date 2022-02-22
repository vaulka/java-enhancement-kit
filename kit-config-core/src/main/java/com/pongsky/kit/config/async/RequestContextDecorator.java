package com.pongsky.kit.config.async;

import com.pongsky.kit.utils.trace.CurrentInfo;
import com.pongsky.kit.utils.trace.CurrentThreadConfig;
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
        CurrentInfo currentInfo = CurrentThreadConfig.getCurrentInfo();
        return () -> {
            try {
                MDC.setContextMap(contextMap);
                CurrentThreadConfig.setCurrentInfo(currentInfo);
                runnable.run();
            } finally {
                MDC.clear();
                CurrentThreadConfig.relCurrentInfo();
            }
        };
    }

}
