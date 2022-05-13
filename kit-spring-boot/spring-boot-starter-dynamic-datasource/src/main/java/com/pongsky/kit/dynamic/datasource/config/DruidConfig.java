package com.pongsky.kit.dynamic.datasource.config;

import com.alibaba.druid.util.Utils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import java.io.IOException;

/**
 * druid 配置
 *
 * @author pengsenhao
 **/
public class DruidConfig {

    /**
     * 去除 druid 监控底部广告
     *
     * @return 拦截器
     * @throws IOException IOException
     * @author pengsenhao
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true")
    public FilterRegistrationBean<Filter> removeDruidAdvertiseFilter() throws IOException {
        String text = Utils.readFromResource("support/http/resources/js/common.js");
        text = text.replaceAll("this.buildFooter\\(\\);", "");
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.addUrlPatterns("/druid/js/common.js");
        String finalText = text;
        registration.setFilter((servletRequest, servletResponse, filterChain) -> {
            servletResponse.resetBuffer();
            servletResponse.setContentType("text/javascript;charset=utf-8");
            servletResponse.getWriter().write(finalText);
        });
        return registration;
    }

}
