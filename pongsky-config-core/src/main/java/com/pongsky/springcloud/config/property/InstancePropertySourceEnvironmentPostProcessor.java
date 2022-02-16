package com.pongsky.springcloud.config.property;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-07 22:10
 **/
public class InstancePropertySourceEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    /**
     * The default order of this post-processor.
     */
    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1;

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        environment.getPropertySources().addFirst(new InstancePropertySource());
    }

}
