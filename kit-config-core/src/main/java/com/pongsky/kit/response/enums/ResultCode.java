package com.pongsky.kit.response.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 响应数据枚举
 *
 * @author pengsenhao
 */
@Getter
public enum ResultCode {

    // 0 表示接口成功

    Success("接口响应成功", 0, "成功"),

    // 100 ~ 199 区间表示参数错误

    GatewayException("网关拦截异常", 114),

    // 200 ~ 299 区间表示用户错误

    // 400 ~ 499 区间表示登录态异常

    // 500 ~ 599 区间表示接口异常

    CircuitBreakerException("断路异常", 502, "服务器出了点小差，请稍后再试"),

    //1000 表示系统异常

    Exception("系统异常", 1000),

    ;

    ResultCode(String details, Integer code) {
        this.details = details;
        this.code = code;
    }

    ResultCode(String details, Integer code, String message) {
        this.details = details;
        this.code = code;
        this.message = message;
    }

    ResultCode(String details, Integer code, List<Class<? extends Exception>> exceptions) {
        this.details = details;
        this.code = code;
        this.exceptions = exceptions.stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    ResultCode(String details, Integer code, String message, List<Class<? extends Exception>> exceptions) {
        this.details = details;
        this.code = code;
        this.message = message;
        this.exceptions = exceptions.stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    ResultCode(String details, Integer code, String message, List<Class<? extends Exception>> exceptions, Boolean isThrow) {
        this.details = details;
        this.code = code;
        this.message = message;
        this.exceptions = exceptions.stream()
                .map(Class::getName)
                .collect(Collectors.toList());
        this.isThrow = isThrow;
    }

    /**
     * 异常详细信息
     */
    private final String details;

    /**
     * 接口响应结果标识码
     */
    private final Integer code;

    /**
     * 接口响应结果信息
     */
    private String message;

    /**
     * 异常列表
     */
    private List<String> exceptions = Collections.emptyList();

    /**
     * 是否直接抛出异常
     */
    private Boolean isThrow = false;

    /**
     * 所有响应结果列表
     */
    private static final List<ResultCode> RESULTS = Arrays.stream(values())
            .collect(Collectors.toList());

    /**
     * 获取响应结果编码
     *
     * @param exception 异常
     * @return 响应结果编码
     */
    public static ResultCode getCode(Exception exception) {
        return RESULTS.stream()
                .filter(r -> r.getExceptions().contains(exception.getClass().getName()))
                .findFirst()
                .orElse(ResultCode.Exception);
    }

}
