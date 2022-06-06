package com.pongsky.kit.desensitization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.pongsky.kit.desensitization.annotation.DesensitizationMark;
import com.pongsky.kit.desensitization.handler.DesensitizationHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据脱敏序列化配置
 *
 * @author pengsenhao
 */
@NoArgsConstructor
@AllArgsConstructor
public class DesensitizationMarkJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizationMark mark;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null
                || mark == null
                || mark.handler().isInterface()
                || mark.handler().isPrimitive()
                || mark.handler().isAnonymousClass()) {
            gen.writeString(value);
            return;
        }
        DesensitizationHandler handler;
        try {
            handler = this.getHandler(mark);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            gen.writeString(value);
            return;
        }
        String val = handler.exec(value);
        gen.writeString(val);
    }

    /**
     * 数据脱敏处理器列表
     */
    private static final Map<String, DesensitizationHandler> HANDLERS = new ConcurrentHashMap<>(16);


    /**
     * 获取数据脱敏处理器
     *
     * @param mark 数据脱敏注解
     * @return 数据脱敏处理器
     */
    private DesensitizationHandler getHandler(DesensitizationMark mark) throws
            NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        if (mark == null) {
            return null;
        }
        Class<? extends DesensitizationHandler> type = mark.handler();
        String key = type.getName();
        DesensitizationHandler desensitizationHandler = HANDLERS.get(key);
        if (desensitizationHandler != null) {
            return desensitizationHandler;
        }
        desensitizationHandler = type.getDeclaredConstructor().newInstance();
        HANDLERS.put(key, desensitizationHandler);
        return desensitizationHandler;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        DesensitizationMark mark = property.getAnnotation(DesensitizationMark.class);
        if (mark != null) {
            return new DesensitizationMarkJsonSerializer(mark);
        }
        return this;
    }

}
