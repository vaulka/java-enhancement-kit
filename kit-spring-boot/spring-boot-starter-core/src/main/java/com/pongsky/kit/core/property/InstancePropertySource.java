package com.pongsky.kit.core.property;

import lombok.NonNull;
import org.springframework.core.env.PropertySource;


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
    public Object getProperty(@NonNull String name) {
        if (!BROKER_ID_PROPERTY_SOURCE_NAME.equals(name)) {
            return null;
        }
        return getSource().getId();
    }

}