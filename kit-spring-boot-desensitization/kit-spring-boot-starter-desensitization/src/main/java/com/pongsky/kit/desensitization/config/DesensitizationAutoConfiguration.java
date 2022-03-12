package com.pongsky.kit.desensitization.config;

import com.pongsky.kit.desensitization.web.aspect.around.DesensitizationAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-03-12 3:54 下午
 */
@Configuration(proxyBeanMethods = false)
@Import({DesensitizationAspect.class})
public class DesensitizationAutoConfiguration {


}
