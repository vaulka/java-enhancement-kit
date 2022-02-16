package com.pongsky.springcloud.exception;

/**
 * 校验异常
 *
 * @author pengsenhao
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -5847524266014715666L;

    public ValidationException(String message) {
        super(message);
    }

}
