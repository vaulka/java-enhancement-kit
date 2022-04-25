package com.pongsky.kit.web.core.autoconfigure;

import com.pongsky.kit.web.core.aspect.ControllerAspect;
import com.pongsky.kit.web.core.config.ControllerAttrConfigurer;
import com.pongsky.kit.web.core.fitler.RepeatedlyReadRequestBodyFilter;
import com.pongsky.kit.web.core.interceptor.ControllerAttrInterceptor;
import com.pongsky.kit.web.core.utils.SpringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Web Core 自动装配
 *
 * @author pengsenhao
 **/
@Configuration(proxyBeanMethods = false)
@Import({RepeatedlyReadRequestBodyFilter.class,
        ControllerAttrConfigurer.class, ControllerAttrInterceptor.class,
        ControllerAspect.class, SpringUtils.class})
public class WebAutoConfiguration {


}
