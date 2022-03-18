package com.pongsky.kit.captcha.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 算数类型
 *
 * @author pengsenhao
 */
@Getter
@AllArgsConstructor
public enum MathType {

    /**
     * 加
     */
    ADD("+"),

    /**
     * 减
     */
    SUBTRACT("-"),

    /**
     * 乘
     */
    MULTIPLY("*"),

    /**
     * 除
     */
    DIVIDE("/"),

    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 所有 算数类型
     */
    public static final List<MathType> ALL = Arrays.stream(values())
            .collect(Collectors.toList());

}
