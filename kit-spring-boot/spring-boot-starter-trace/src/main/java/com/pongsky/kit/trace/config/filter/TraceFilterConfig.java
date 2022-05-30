package com.pongsky.kit.trace.config.filter;

import com.pongsky.kit.trace.filter.TraceFilter;
import com.pongsky.kit.trace.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 拦截器配置
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
public class TraceFilterConfig {

    private final ApplicationProperties applicationProperties;

    /**
     * 追踪链路
     *
     * @return 追踪链路
     */
    @Bean
    public FilterRegistrationBean<TraceFilter> xssDefenseFilterRegistrationBean() {
        FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<>();
        TraceFilter filter = new TraceFilter(applicationProperties);
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName(filter.getClass().getName());
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

}
