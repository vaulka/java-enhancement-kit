package com.pongsky.kit.ip.autoconfigure;

import com.pongsky.kit.ip.config.IpSearcherConfig;
import com.pongsky.kit.ip.properties.IpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * IP 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@Import({IpSearcherConfig.class})
@EnableConfigurationProperties({IpProperties.class})
public class IpAutoConfiguration {


}
