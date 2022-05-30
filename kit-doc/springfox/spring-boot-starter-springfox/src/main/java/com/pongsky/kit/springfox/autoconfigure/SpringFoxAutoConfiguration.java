package com.pongsky.kit.springfox.autoconfigure;

import com.pongsky.kit.springfox.config.SpringFoxConfig;
import com.pongsky.kit.springfox.config.SwaggerResourceConfig;
import com.pongsky.kit.springfox.config.SwaggerRouteLocator;
import com.pongsky.kit.springfox.properties.SpringFoxProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * SpringFox 自动装配
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SpringFoxProperties.class})
@Import({SpringFoxConfig.class, SwaggerResourceConfig.class, SwaggerRouteLocator.class})
public class SpringFoxAutoConfiguration {


}
