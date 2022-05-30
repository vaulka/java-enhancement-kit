package com.pongsky.kit.web.core.config;

import com.pongsky.kit.web.core.filter.RepeatedlyReadRequestBodyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Request Body 数据多次读取 配置
 *
 * @author pengsenhao
 **/
public class RepeatedlyReadRequestBodyFilterConfig {

    /**
     * Request Body 数据多次读取
     *
     * @return Request Body 数据多次读取
     */
    @Bean
    public FilterRegistrationBean<RepeatedlyReadRequestBodyFilter> repeatedlyReadRequestBodyFilterRegistrationBean() {
        FilterRegistrationBean<RepeatedlyReadRequestBodyFilter> registration = new FilterRegistrationBean<>();
        RepeatedlyReadRequestBodyFilter filter = new RepeatedlyReadRequestBodyFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName(filter.getClass().getName());
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }

}
