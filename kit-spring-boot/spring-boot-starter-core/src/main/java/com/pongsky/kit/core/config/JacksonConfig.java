package com.pongsky.kit.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Jackson 配置
 *
 * @author pengsenhao
 */
public class JacksonConfig {

    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * 替换默认的的 JavaTimeModule
     * <p>
     * 补充 Jackson 序列化、反序列化
     * <p>
     * 兼容 Feign 序列化、反序列化
     * <ul>
     * <li>Long to String（防止前端 js 精度丢失）</li>
     * <li>Double to String（防止前端 js 精度丢失）</li>
     * </ul>
     * 统一时间格式：
     * <ul>
     * <li>Date to yyyy-MM-dd HH:mm:ss</li>
     * <li>LocalDateTime to yyyy-MM-dd HH:mm:ss</li>
     * <li>LocalDate to yyyy-MM-dd</li>
     * <li>LocalTime to HH:mm:ss</li>
     * </ul>
     *
     * @return JavaTimeModule
     * @author pengsenhao
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Double.class, ToStringSerializer.instance)
                .addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN)))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)))
                .addDeserializer(Date.class, new DateDeserializers.DateDeserializer())
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)));
        return javaTimeModule;
    }

    /**
     * 最小侵入性去修改 ObjectMapper 序列化、反序列化等规则
     * <p>
     * 创建 ObjectMapper 还是由 Spring 去创建，创建后进行增强
     *
     * @param builder builder
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        return objectMapper
                .setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
