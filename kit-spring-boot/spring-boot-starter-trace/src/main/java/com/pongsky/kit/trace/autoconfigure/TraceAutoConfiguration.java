package com.pongsky.kit.trace.autoconfigure;

import com.pongsky.kit.trace.config.async.AsyncConfig;
import com.pongsky.kit.trace.config.feign.FeignConfig;
import com.pongsky.kit.trace.config.filter.FilterConfig;
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
@Import({AsyncConfig.class, FeignConfig.class, FilterConfig.class})
public class TraceAutoConfiguration {


}
