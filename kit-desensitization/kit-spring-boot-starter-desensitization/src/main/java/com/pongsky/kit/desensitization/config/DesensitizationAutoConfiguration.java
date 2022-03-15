package com.pongsky.kit.desensitization.config;

import com.pongsky.kit.desensitization.web.aspect.around.DesensitizationAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 数据脱敏 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@Import({DesensitizationAspect.class})
public class DesensitizationAutoConfiguration {


}
