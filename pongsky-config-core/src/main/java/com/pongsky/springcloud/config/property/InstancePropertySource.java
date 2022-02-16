package com.pongsky.springcloud.config.property;

import org.springframework.core.env.PropertySource;
import org.springframework.core.log.LogMessage;

import javax.annotation.Nonnull;

/**
 * 应用实例ID属性配置信息
 *
 * @author pengsenhao
 */
public class InstancePropertySource extends PropertySource<Instance> {

    /**
     * 自定义属性 key
     */
    private static final String BROKER_ID_PROPERTY_SOURCE_NAME = "brokerId";

    public InstancePropertySource() {
        super(BROKER_ID_PROPERTY_SOURCE_NAME, new Instance());
    }

    @Override
    public Object getProperty(@Nonnull String name) {
        if (!BROKER_ID_PROPERTY_SOURCE_NAME.equals(name)) {
            return null;
        }
        logger.trace(LogMessage.format("Generating brokerId property for '%s'", name));
        return getSource().getId();
    }

}