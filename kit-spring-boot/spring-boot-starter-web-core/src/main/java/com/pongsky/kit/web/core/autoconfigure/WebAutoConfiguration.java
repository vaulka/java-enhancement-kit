package com.pongsky.kit.web.core.autoconfigure;

import com.pongsky.kit.web.core.aspect.ControllerAspect;
import com.pongsky.kit.web.core.config.ControllerAttrConfigurer;
import com.pongsky.kit.web.core.config.CorsFilterConfig;
import com.pongsky.kit.web.core.config.RepeatedlyReadRequestBodyFilterConfig;
import com.pongsky.kit.web.core.config.XssDefenseFilterConfig;
import com.pongsky.kit.web.core.interceptor.ControllerAttrInterceptor;
import com.pongsky.kit.web.core.properties.XssDefenseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Web Core 自动装配
 *
 * @author pengsenhao
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({XssDefenseProperties.class})
@Import({CorsFilterConfig.class, RepeatedlyReadRequestBodyFilterConfig.class, XssDefenseFilterConfig.class,
        ControllerAttrConfigurer.class, ControllerAttrInterceptor.class,
        ControllerAspect.class})
public class WebAutoConfiguration {


}
