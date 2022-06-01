package com.pongsky.kit.common.exception;

/**
 * 校验异常
 *
 * @author pengsenhao
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -6511791210548696965L;

    public ValidationException(String message) {
        super(message);
    }

}
