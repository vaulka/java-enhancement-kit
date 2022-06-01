package com.pongsky.kit.common.autoconfigure;

import com.pongsky.kit.common.utils.SpringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 公共配置 自动装配
 *
 * @author pengsenhao
 **/
@Configuration(proxyBeanMethods = false)
@Import({SpringUtils.class})
public class CommonAutoConfiguration {


}
