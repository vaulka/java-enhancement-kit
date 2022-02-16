package com.pongsky.springcloud.config.async;

import com.pongsky.springcloud.utils.trace.CurrentInfo;
import com.pongsky.springcloud.utils.trace.CurrentThreadConfig;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-01-21 4:05 下午
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
