package com.pongsky.kit.storage.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.pongsky.kit.common.utils.SpringUtils;
import com.pongsky.kit.storage.annotation.StorageMark;
import com.pongsky.kit.storage.properties.StorageProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * 云存储序列化配置
 *
 * @author pengsenhao
 */
@NoArgsConstructor
@AllArgsConstructor
public class StorageMarkJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private StorageMark mark;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || mark == null) {
            gen.writeString(value);
            return;
        }
        ApplicationContext applicationContext = SpringUtils.getApplicationContext();
        StorageProperties properties = applicationContext.getBean(StorageProperties.class);
        if (!properties.getIsEnableResourceMark()) {
            gen.writeString(value);
            return;
        }
        gen.writeString(properties.getBaseUri() + value);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        StorageMark mark = property.getAnnotation(StorageMark.class);
        if (mark != null) {
            return new StorageMarkJsonSerializer(mark);
        }
        return this;
    }

}
