package com.pongsky.kit.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Spring MVC 配置
 * <p>
 * Feign 处理 GET 请求，会将请求参数 toString 进行处理（feign.ReflectiveFeign.BuildTemplateByResolvingArgs#addQueryMapQueryParameters）
 * 需要兼容 {@link Date#toString()}、{@link LocalDate#toString()}、{@link LocalTime#toString()}、{@link LocalDateTime#toString()} 的情况，复杂对象查询就不能使用 GET 请求。
 *
 * @author pengsenhao
 */
public class SpringMvcConfig {

    /**
     * {@link Date#toString()} 标准格式
     */
    private static final String DATE_TO_STRING_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    /**
     * 日期时间 标准格式
     */
    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * {@link LocalDateTime#toString()} 标准格式 去除 T
     */
    private static final String DEFAULT_DATE_TIME_TO_STRING_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 日期 标准格式
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间 标准格式
     */
    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * {@link LocalTime#toString()} 标准格式
     */
    private static final String DEFAULT_TIME_TO_STRING_PATTERN = "HH:mm:ss.SSS";

    interface StringToDateConverter extends Converter<String, Date> {

    }

    /**
     * 补充 Spring MVC String to Date 转换器
     * <p>
     * Date to yyyy-MM-dd HH:mm:ss
     *
     * @return 补充 Spring MVC String to Date 转换器
     */
    @Bean
    public StringToDateConverter dateConverter() {
        return source -> {
            try {
                return new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN).parse(source);
            } catch (ParseException e1) {
                try {
                    // 尝试解析 toString 格式
                    return new SimpleDateFormat(DATE_TO_STRING_PATTERN, Locale.ENGLISH).parse(source);
                } catch (ParseException e2) {
                    throw new IllegalArgumentException(e2.getLocalizedMessage(), e2);
                }
            }
        };
    }

    interface StringToLocalDateConverter extends Converter<String, LocalDate> {

    }

    /**
     * 补充 Spring MVC String to LocalDate 转换器
     * <p>
     * LocalDate to yyyy-MM-dd
     *
     * @return 补充 Spring MVC String to LocalDate 转换器
     */
    @Bean
    public StringToLocalDateConverter localDateConverter() {
        return source -> LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN));
    }

    interface StringToLocalTimeConverter extends Converter<String, LocalTime> {

    }

    /**
     * 补充 Spring MVC String to LocalTime 转换器
     * <p>
     * LocalTime to HH:mm:ss
     *
     * @return 补充 Spring MVC String to LocalTime 转换器
     */
    @Bean
    public StringToLocalTimeConverter localTimeConverter() {
        return source -> {
            try {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN));
            } catch (DateTimeParseException e1) {
                try {
                    // 尝试解析 toString 格式
                    return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_TO_STRING_PATTERN));
                } catch (DateTimeParseException e2) {
                    throw new IllegalArgumentException(e2.getLocalizedMessage(), e2);
                }
            }
        };
    }

    interface StringToLocalDateTimeConverter extends Converter<String, LocalDateTime> {

    }

    /**
     * 补充 Spring MVC String to LocalDateTime 转换器
     * <p>
     * LocalDateTime to yyyy-MM-dd HH:mm:ss
     *
     * @return 补充 Spring MVC String to LocalDateTime 转换器
     */
    @Bean
    public StringToLocalDateTimeConverter localDateTimeConverter() {
        return source -> {
            source = source.replace("T", " ");
            try {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
            } catch (DateTimeParseException e1) {
                try {
                    // 尝试解析 toString 格式
                    return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_TO_STRING_PATTERN));
                } catch (DateTimeParseException e2) {
                    throw new IllegalArgumentException(e2.getLocalizedMessage(), e2);
                }
            }
        };
    }

}
