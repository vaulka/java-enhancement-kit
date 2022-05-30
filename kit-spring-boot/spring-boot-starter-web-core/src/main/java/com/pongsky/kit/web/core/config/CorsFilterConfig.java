package com.pongsky.kit.web.core.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * 跨域访问 配置
 *
 * @author pengsenhao
 **/
public class CorsFilterConfig {

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

}
