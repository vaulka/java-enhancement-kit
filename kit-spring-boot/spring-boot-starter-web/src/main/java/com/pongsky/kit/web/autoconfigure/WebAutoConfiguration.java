package com.pongsky.kit.web.autoconfigure;

import com.pongsky.kit.web.aspect.ControllerAspect;
import com.pongsky.kit.web.config.ControllerAttrConfigurer;
import com.pongsky.kit.web.fitler.ReplaceStreamFilter;
import com.pongsky.kit.web.interceptor.ControllerAttrInterceptor;
import com.pongsky.kit.web.utils.SpringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Web 自动装配
 *
 * @author pengsenhao
 **/
@Configuration(proxyBeanMethods = false)
@Import({ReplaceStreamFilter.class,
        ControllerAttrConfigurer.class, ControllerAttrInterceptor.class,
        ControllerAspect.class, SpringUtils.class})
public class WebAutoConfiguration {


}
