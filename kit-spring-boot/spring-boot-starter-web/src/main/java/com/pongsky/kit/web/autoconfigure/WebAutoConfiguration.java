package com.pongsky.kit.web.autoconfigure;

import com.pongsky.kit.web.aspect.ControllerAspect;
import com.pongsky.kit.web.fitler.ReplaceStreamFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Web 自动装配
 *
 * @author pengsenhao
 **/
@Configuration(proxyBeanMethods = false)
@Import({ReplaceStreamFilter.class, ControllerAspect.class})
public class WebAutoConfiguration {


}
