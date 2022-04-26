package com.pongsky.kit.web.core.config;

import com.pongsky.kit.web.core.filter.RepeatedlyReadRequestBodyFilter;
import com.pongsky.kit.web.core.filter.XssDefenseFilter;
import com.pongsky.kit.web.core.properties.XssDefenseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * 拦截器配置
 *
 * @author pengsenhao
 **/
public class FilterConfig {

    /**
     * 配置跨域访问
     *
     * @param source source
     * @return 配置跨域访问
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(UrlBasedCorsConfigurationSource source) {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        CorsFilter filter = new CorsFilter(source);
        registration.setFilter(filter);
        registration.setName(filter.getClass().getName());
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

    /**
     * 配置跨域访问
     * <p>
     * 兼容 Spring Security 跨域访问
     *
     * @return 配置跨域访问
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        // 允许客户端发送 Cookie 等凭证信息
        cors.setAllowCredentials(true);
        // 允许所有原始域进行跨域访问
        cors.setAllowedOrigins(Collections.singletonList("*"));
        // 允许所所有请求头携带访问
        cors.setAllowedHeaders(Collections.singletonList("*"));
        // 允许所有请求方式访问
        cors.setAllowedMethods(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 跨域访问对所有接口生效
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

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

    /**
     * XSS 防御
     *
     * @param properties XSS 防御 参数配置
     * @return XSS 防御
     */
    @Bean
    @ConditionalOnProperty(name = XssDefenseProperties.ENABLED_PROPERTIES, havingValue = "true", matchIfMissing = true)
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
