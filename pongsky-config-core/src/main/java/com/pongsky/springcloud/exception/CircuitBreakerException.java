package com.pongsky.springcloud.exception;

/**
 * 断路异常
 *
 * @author pengsenhao
 */
public class CircuitBreakerException extends RuntimeException {

    private static final long serialVersionUID = -6628643616928219648L;

    public CircuitBreakerException() {
        super();
    }

}
