package com.pongsky.kit.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Spring MVC 配置
 *
 * @author pengsenhao
 */
public class SpringMvcConfig {

    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

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
            } catch (ParseException e) {
                return null;
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
        return source -> LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN));
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
        return source -> LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
    }

}
