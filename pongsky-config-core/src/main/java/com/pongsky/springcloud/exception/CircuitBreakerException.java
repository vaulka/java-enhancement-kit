package com.pongsky.springcloud.exception;

/**
 * 断路异常
 *
 * @author pengsenhao
 * @create 2021-02-17
 */
public class CircuitBreakerException extends RuntimeException {

    private static final long serialVersionUID = -6628643616928219648L;

    public CircuitBreakerException() {
        super();
    }

}
