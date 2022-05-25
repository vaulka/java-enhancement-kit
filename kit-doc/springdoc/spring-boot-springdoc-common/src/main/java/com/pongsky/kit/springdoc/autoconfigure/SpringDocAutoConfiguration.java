package com.pongsky.kit.springdoc.autoconfigure;

import com.pongsky.kit.springdoc.config.SpringDocConfig;
import com.pongsky.kit.springdoc.config.SwaggerRouteLocator;
import com.pongsky.kit.springdoc.properties.SpringDocProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * SpringDoc 自动装配
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SpringDocProperties.class})
@Import({SpringDocConfig.class, SwaggerRouteLocator.class})
public class SpringDocAutoConfiguration {


}
