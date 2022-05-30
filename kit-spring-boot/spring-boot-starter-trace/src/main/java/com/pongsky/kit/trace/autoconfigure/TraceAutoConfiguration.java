package com.pongsky.kit.trace.autoconfigure;

import com.pongsky.kit.trace.config.async.TraceAsyncConfig;
import com.pongsky.kit.trace.config.feign.TraceFeignConfig;
import com.pongsky.kit.trace.config.filter.TraceFilterConfig;
import com.pongsky.kit.trace.properties.ApplicationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 全局响应 自动装配
 *
 * @author pengsenhao
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ApplicationProperties.class)
@Import({TraceAsyncConfig.class, TraceFeignConfig.class, TraceFilterConfig.class})
public class TraceAutoConfiguration {


}
