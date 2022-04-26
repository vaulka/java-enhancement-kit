package com.pongsky.kit.web.core.autoconfigure;

import com.pongsky.kit.web.core.aspect.ControllerAspect;
import com.pongsky.kit.web.core.config.ControllerAttrConfigurer;
import com.pongsky.kit.web.core.config.FilterConfig;
import com.pongsky.kit.web.core.interceptor.ControllerAttrInterceptor;
import com.pongsky.kit.web.core.properties.XssDefenseProperties;
import com.pongsky.kit.web.core.utils.SpringUtils;
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
@Import({FilterConfig.class,
        ControllerAttrConfigurer.class, ControllerAttrInterceptor.class,
        ControllerAspect.class, SpringUtils.class})
public class WebAutoConfiguration {


}
