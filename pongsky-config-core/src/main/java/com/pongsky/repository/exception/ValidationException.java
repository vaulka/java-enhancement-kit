package com.pongsky.repository.exception;

/**
 * 校验异常
 *
 * @author pengsenhao
 * @create 2021-02-11
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -5847524266014715666L;

    public ValidationException(String message) {
        super(message);
    }

}
