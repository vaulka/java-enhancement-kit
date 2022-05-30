package com.pongsky.kit.web.core.config;

import com.pongsky.kit.web.core.filter.XssDefenseFilter;
import com.pongsky.kit.web.core.properties.XssDefenseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * XSS 防御 配置
 *
 * @author pengsenhao
 **/
@ConditionalOnProperty(name = XssDefenseProperties.ENABLED_PROPERTIES, havingValue = "true", matchIfMissing = true)
public class XssDefenseFilterConfig {

    /**
     * XSS 防御
     *
     * @param properties XSS 防御 参数配置
     * @return XSS 防御
     */
    @Bean
    public FilterRegistrationBean<XssDefenseFilter> xssDefenseFilterRegistrationBean(XssDefenseProperties properties) {
        FilterRegistrationBean<XssDefenseFilter> registration = new FilterRegistrationBean<>();
        XssDefenseFilter filter = new XssDefenseFilter(properties.getExcludes());
        registration.setFilter(filter);
        registration.addUrlPatterns(properties.getUrlPatterns().toArray(new String[0]));
        registration.setName(filter.getClass().getName());
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

}
