package com.pongsky.kit.trace.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步配置
 *
 * @author pengsenhao
 **/
@EnableAsync
public class TraceAsyncConfig {

    /**
     * 异步标识
     */
    public static final String ASYNC_MARK = "asyncThreadPool";

    @Bean(ASYNC_MARK)
    public Executor asyncThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(10);
        // 最大线程数
        executor.setMaxPoolSize(20);
        // 缓冲队列数
        executor.setQueueCapacity(200);
        // 线程空置时间
        executor.setKeepAliveSeconds(60);
        // 线程池前缀
        executor.setThreadNamePrefix("Process-Pool-");
        executor.setTaskDecorator(new RequestContextDecorator());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
